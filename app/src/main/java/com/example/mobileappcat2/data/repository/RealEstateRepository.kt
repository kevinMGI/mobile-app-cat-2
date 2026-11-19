package com.example.mobileappcat2.data.repository

import com.example.mobileappcat2.data.local.AppDatabase
import com.example.mobileappcat2.data.local.dao.MaintenanceDao
import com.example.mobileappcat2.data.local.dao.PaymentDao
import com.example.mobileappcat2.data.local.dao.PropertyDao
import com.example.mobileappcat2.data.local.dao.TenantDao
import com.example.mobileappcat2.data.local.entity.MaintenanceRequestEntity
import com.example.mobileappcat2.data.local.entity.PaymentEntity
import com.example.mobileappcat2.data.local.entity.PropertyEntity
import com.example.mobileappcat2.data.local.entity.TenantEntity
import kotlinx.coroutines.flow.Flow

class RealEstateRepository(private val database: AppDatabase) {

    private val propertyDao: PropertyDao = database.propertyDao()
    private val tenantDao: TenantDao = database.tenantDao()
    private val paymentDao: PaymentDao = database.paymentDao()
    private val maintenanceDao: MaintenanceDao = database.maintenanceDao()

    // Properties
    fun getAllProperties(): Flow<List<PropertyEntity>> = propertyDao.getAllProperties()

    suspend fun getPropertyById(id: Int): PropertyEntity? = propertyDao.getPropertyById(id)

    suspend fun insertProperty(property: PropertyEntity): Long = propertyDao.insertProperty(property)

    suspend fun updateProperty(property: PropertyEntity) = propertyDao.updateProperty(property)

    suspend fun deleteProperty(property: PropertyEntity) = propertyDao.deleteProperty(property)

    suspend fun deletePropertyById(id: Int) = propertyDao.deletePropertyById(id)

    // Tenants
    fun getAllTenants(): Flow<List<TenantEntity>> = tenantDao.getAllTenants()

    suspend fun getTenantById(id: Int): TenantEntity? = tenantDao.getTenantById(id)

    fun getTenantsForProperty(propertyId: Int): Flow<List<TenantEntity>> = tenantDao.getTenantsForProperty(propertyId)

    suspend fun insertTenant(tenant: TenantEntity): Long = tenantDao.insertTenant(tenant)

    suspend fun updateTenant(tenant: TenantEntity) = tenantDao.updateTenant(tenant)

    suspend fun deleteTenant(tenant: TenantEntity) = tenantDao.deleteTenant(tenant)

    suspend fun deleteTenantById(id: Int) = tenantDao.deleteTenantById(id)

    // Payments
    fun getAllPayments(): Flow<List<PaymentEntity>> = paymentDao.getAllPayments()

    suspend fun getPaymentById(id: Int): PaymentEntity? = paymentDao.getPaymentById(id)

    fun getPaymentsForTenant(tenantId: Int): Flow<List<PaymentEntity>> = paymentDao.getPaymentsForTenant(tenantId)

    suspend fun insertPayment(payment: PaymentEntity): Long = paymentDao.insertPayment(payment)

    suspend fun updatePayment(payment: PaymentEntity) = paymentDao.updatePayment(payment)

    suspend fun deletePayment(payment: PaymentEntity) = paymentDao.deletePayment(payment)

    suspend fun deletePaymentById(id: Int) = paymentDao.deletePaymentById(id)

    // Maintenance Requests
    fun getAllMaintenanceRequests(): Flow<List<MaintenanceRequestEntity>> = maintenanceDao.getAllRequests()

    suspend fun getMaintenanceRequestById(id: Int): MaintenanceRequestEntity? = maintenanceDao.getRequestById(id)

    fun getRequestsForProperty(propertyId: Int): Flow<List<MaintenanceRequestEntity>> = maintenanceDao.getRequestsForProperty(propertyId)

    suspend fun insertMaintenanceRequest(request: MaintenanceRequestEntity): Long = maintenanceDao.insertRequest(request)

    suspend fun updateMaintenanceRequest(request: MaintenanceRequestEntity) = maintenanceDao.updateRequest(request)

    suspend fun deleteMaintenanceRequest(request: MaintenanceRequestEntity) = maintenanceDao.deleteRequest(request)

    suspend fun deleteMaintenanceRequestById(id: Int) = maintenanceDao.deleteRequestById(id)

    // Re-seed database helper for testing / resets
    suspend fun resetDemoData() {
        AppDatabase.populateSeedData(database)
    }
}
