package com.example.mobileappcat2.ui.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileappcat2.data.local.AppDatabase
import com.example.mobileappcat2.data.local.entity.MaintenanceRequestEntity
import com.example.mobileappcat2.data.local.entity.PaymentEntity
import com.example.mobileappcat2.data.local.entity.PropertyEntity
import com.example.mobileappcat2.data.local.entity.TenantEntity
import com.example.mobileappcat2.data.repository.RealEstateRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RealEstateViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RealEstateRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = RealEstateRepository(database)
    }

    // Search and Filter State
    val propertySearchQuery = MutableStateFlow("")
    val propertyTypeFilter = MutableStateFlow("All")

    val tenantSearchQuery = MutableStateFlow("")
    val tenantStatusFilter = MutableStateFlow("All")

    val paymentSearchQuery = MutableStateFlow("")
    val paymentStatusFilter = MutableStateFlow("All")

    val maintenanceSearchQuery = MutableStateFlow("")
    val maintenanceStatusFilter = MutableStateFlow("All")

    // UI Message / Toast state
    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    // Base Data Flows
    val allProperties: StateFlow<List<PropertyEntity>> = repository.getAllProperties()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTenants: StateFlow<List<TenantEntity>> = repository.getAllTenants()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPayments: StateFlow<List<PaymentEntity>> = repository.getAllPayments()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allMaintenanceRequests: StateFlow<List<MaintenanceRequestEntity>> = repository.getAllMaintenanceRequests()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered Properties
    val filteredProperties: StateFlow<List<PropertyEntity>> = combine(
        allProperties,
        propertySearchQuery,
        propertyTypeFilter
    ) { properties, query, filter ->
        properties.filter { prop ->
            val matchesQuery = prop.title.contains(query, ignoreCase = true) ||
                    prop.address.contains(query, ignoreCase = true)
            val matchesFilter = filter == "All" || prop.propertyType.equals(filter, ignoreCase = true)
            matchesQuery && matchesFilter
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered Tenants
    val filteredTenants: StateFlow<List<TenantEntity>> = combine(
        allTenants,
        tenantSearchQuery,
        tenantStatusFilter
    ) { tenants, query, filter ->
        tenants.filter { t ->
            val matchesQuery = t.fullName.contains(query, ignoreCase = true) ||
                    t.propertyName.contains(query, ignoreCase = true) ||
                    t.unitNumber.contains(query, ignoreCase = true)
            val matchesFilter = filter == "All" || t.status.equals(filter, ignoreCase = true)
            matchesQuery && matchesFilter
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered Payments
    val filteredPayments: StateFlow<List<PaymentEntity>> = combine(
        allPayments,
        paymentSearchQuery,
        paymentStatusFilter
    ) { payments, query, filter ->
        payments.filter { p ->
            val matchesQuery = p.tenantName.contains(query, ignoreCase = true) ||
                    p.propertyName.contains(query, ignoreCase = true) ||
                    p.transactionRef.contains(query, ignoreCase = true)
            val matchesFilter = filter == "All" || p.status.equals(filter, ignoreCase = true)
            matchesQuery && matchesFilter
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered Maintenance Requests
    val filteredMaintenanceRequests: StateFlow<List<MaintenanceRequestEntity>> = combine(
        allMaintenanceRequests,
        maintenanceSearchQuery,
        maintenanceStatusFilter
    ) { requests, query, filter ->
        requests.filter { req ->
            val matchesQuery = req.title.contains(query, ignoreCase = true) ||
                    req.propertyName.contains(query, ignoreCase = true) ||
                    req.tenantName.contains(query, ignoreCase = true)
            val matchesFilter = filter == "All" || req.status.equals(filter, ignoreCase = true)
            matchesQuery && matchesFilter
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Metrics for Dashboard
    val totalRevenue: StateFlow<Double> = allPayments.map { payments ->
        payments.filter { it.status == "Paid" }.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val pendingRevenue: StateFlow<Double> = allPayments.map { payments ->
        payments.filter { it.status != "Paid" }.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val activeMaintenanceCount: StateFlow<Int> = allMaintenanceRequests.map { reqs ->
        reqs.count { it.status != "Resolved" }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // --- Property CRUD Operations ---

    fun saveProperty(
        id: Int = 0,
        title: String,
        address: String,
        type: String,
        units: Int,
        rent: Double,
        status: String,
        description: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            if (title.isBlank()) {
                _snackbarMessage.emit("Property title cannot be empty")
                return@launch
            }

            if (address.isBlank()) {
                _snackbarMessage.emit("Property address cannot be empty")
                return@launch
            }

            if (units <= 0) {
                _snackbarMessage.emit("Number of units must be greater than zero")
                return@launch
            }

            if (rent <= 0 || !rent.isFinite()) {
                _snackbarMessage.emit("Enter a valid rent amount greater than zero")
                return@launch
            }

            if (type !in listOf("Apartment", "Single Family", "Commercial", "Villa")) {
                _snackbarMessage.emit("Please select a valid property type")
                return@launch
            }

            if (status !in listOf("Occupied", "Vacant", "Maintenance")) {
                _snackbarMessage.emit("Please select a valid property status")
                return@launch
            }

            val entity = PropertyEntity(
                id = id,
                title = title.trim(),
                address = address.trim(),
                propertyType = type,
                unitsCount = units,
                monthlyRent = rent,
                status = status,
                description = description.trim()
            )

            if (id == 0) {
                repository.insertProperty(entity)
                _snackbarMessage.emit("Property added successfully!")
            } else {
                repository.updateProperty(entity)
                _snackbarMessage.emit("Property updated successfully!")
            }

            onSuccess()
        }
    }


    fun deleteProperty(property: PropertyEntity) {
        viewModelScope.launch {
            repository.deleteProperty(property)
            _snackbarMessage.emit("Property '${property.title}' deleted")
        }
    }

    // --- Tenant CRUD Operations ---
    fun saveTenant(
        id: Int = 0,
        propertyId: Int,
        propertyName: String,
        fullName: String,
        email: String,
        phone: String,
        unitNumber: String,
        rentAmount: Double,
        leaseStart: String,
        leaseEnd: String,
        status: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            if (fullName.isBlank()) {
                _snackbarMessage.emit("Tenant full name is required")
                return@launch
            }
            if (email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                _snackbarMessage.emit("Please enter a valid email address")
                return@launch
            }
            if (phone.isBlank()) {
                _snackbarMessage.emit("Phone number is required")
                return@launch
            }
            if (rentAmount <= 0) {
                _snackbarMessage.emit("Rent amount must be greater than zero")
                return@launch
            }

            val entity = TenantEntity(
                id = id,
                propertyId = propertyId,
                propertyName = propertyName,
                fullName = fullName.trim(),
                email = email.trim(),
                phone = phone.trim(),
                unitNumber = unitNumber.trim(),
                rentAmount = rentAmount,
                leaseStartDate = leaseStart.trim(),
                leaseEndDate = leaseEnd.trim(),
                status = status
            )

            if (id == 0) {
                repository.insertTenant(entity)
                _snackbarMessage.emit("Tenant registered successfully!")
            } else {
                repository.updateTenant(entity)
                _snackbarMessage.emit("Tenant details updated!")
            }
            onSuccess()
        }
    }

    fun deleteTenant(tenant: TenantEntity) {
        viewModelScope.launch {
            repository.deleteTenant(tenant)
            _snackbarMessage.emit("Tenant '${tenant.fullName}' removed")
        }
    }

    // --- Payment CRUD Operations ---
    fun savePayment(
        id: Int = 0,
        tenantId: Int,
        tenantName: String,
        propertyName: String,
        amount: Double,
        paymentDate: String,
        paymentMethod: String,
        status: String,
        ref: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            if (tenantName.isBlank()) {
                _snackbarMessage.emit("Tenant name is required")
                return@launch
            }
            if (amount <= 0) {
                _snackbarMessage.emit("Payment amount must be greater than zero")
                return@launch
            }

            val entity = PaymentEntity(
                id = id,
                tenantId = tenantId,
                tenantName = tenantName.trim(),
                propertyName = propertyName.trim(),
                amount = amount,
                paymentDate = paymentDate.trim(),
                paymentMethod = paymentMethod,
                status = status,
                transactionRef = ref.ifBlank { "REF-${System.currentTimeMillis().toString().takeLast(6)}" }
            )

            if (id == 0) {
                repository.insertPayment(entity)
                _snackbarMessage.emit("Payment transaction logged!")
            } else {
                repository.updatePayment(entity)
                _snackbarMessage.emit("Payment record updated!")
            }
            onSuccess()
        }
    }

    fun deletePayment(payment: PaymentEntity) {
        viewModelScope.launch {
            repository.deletePayment(payment)
            _snackbarMessage.emit("Payment record deleted")
        }
    }

    // --- Maintenance Operations ---
    fun saveMaintenanceRequest(
        id: Int = 0,
        propertyId: Int,
        propertyName: String,
        tenantName: String,
        title: String,
        category: String,
        priority: String,
        status: String,
        dateReported: String,
        description: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            if (title.isBlank()) {
                _snackbarMessage.emit("Maintenance issue title is required")
                return@launch
            }
            val entity = MaintenanceRequestEntity(
                id = id,
                propertyId = propertyId,
                propertyName = propertyName.trim(),
                tenantName = tenantName.trim(),
                title = title.trim(),
                category = category,
                priority = priority,
                status = status,
                dateReported = dateReported.trim(),
                description = description.trim()
            )

            if (id == 0) {
                repository.insertMaintenanceRequest(entity)
                _snackbarMessage.emit("Maintenance request created!")
            } else {
                repository.updateMaintenanceRequest(entity)
                _snackbarMessage.emit("Maintenance request updated!")
            }
            onSuccess()
        }
    }

    fun deleteMaintenanceRequest(request: MaintenanceRequestEntity) {
        viewModelScope.launch {
            repository.deleteMaintenanceRequest(request)
            _snackbarMessage.emit("Maintenance ticket resolved & deleted")
        }
    }

    fun resetDemoData() {
        viewModelScope.launch {
            repository.resetDemoData()
            _snackbarMessage.emit("Demo data restored successfully!")
        }
    }
}
