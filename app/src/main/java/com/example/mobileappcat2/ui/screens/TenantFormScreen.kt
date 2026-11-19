package com.example.mobileappcat2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mobileappcat2.ui.viewmodel.RealEstateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantFormScreen(
    tenantId: Int? = null,
    viewModel: RealEstateViewModel,
    onBackClick: () -> Unit
) {
    val tenants by viewModel.allTenants.collectAsState()
    val properties by viewModel.allProperties.collectAsState()

    val existingTenant = remember(tenants, tenantId) {
        tenants.find { it.id == tenantId }
    }

    var selectedPropertyId by remember(existingTenant, properties) {
        mutableIntStateOf(existingTenant?.propertyId ?: properties.firstOrNull()?.id ?: 0)
    }
    var selectedPropertyName by remember(existingTenant, properties) {
        mutableStateOf(existingTenant?.propertyName ?: properties.firstOrNull()?.title ?: "")
    }

    var fullName by remember(existingTenant) { mutableStateOf(existingTenant?.fullName ?: "") }
    var email by remember(existingTenant) { mutableStateOf(existingTenant?.email ?: "") }
    var phone by remember(existingTenant) { mutableStateOf(existingTenant?.phone ?: "") }
    var unitNumber by remember(existingTenant) { mutableStateOf(existingTenant?.unitNumber ?: "") }
    var rentText by remember(existingTenant) { mutableStateOf(existingTenant?.rentAmount?.let { if (it == 0.0) "" else it.toInt().toString() } ?: "") }
    var leaseStart by remember(existingTenant) { mutableStateOf(existingTenant?.leaseStartDate ?: "2024-01-01") }
    var leaseEnd by remember(existingTenant) { mutableStateOf(existingTenant?.leaseEndDate ?: "2025-12-31") }
    var status by remember(existingTenant) { mutableStateOf(existingTenant?.status ?: "Active") }

    val statusOptions = listOf("Active", "Pending", "Past")

    var propertyDropdownExpanded by remember { mutableStateOf(false) }
    var statusDropdownExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (tenantId == null) "Register New Tenant" else "Edit Tenant Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Tenant & Lease Registration",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Tenant Full Name *") },
                placeholder = { Text("e.g. Jane Wanjiru") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Property Selection Dropdown
            ExposedDropdownMenuBox(
                expanded = propertyDropdownExpanded,
                onExpandedChange = { propertyDropdownExpanded = !propertyDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = selectedPropertyName.ifBlank { "Select Assigned Property" },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Assigned Property *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = propertyDropdownExpanded) },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                ExposedDropdownMenu(
                    expanded = propertyDropdownExpanded,
                    onDismissRequest = { propertyDropdownExpanded = false }
                ) {
                    properties.forEach { prop ->
                        DropdownMenuItem(
                            text = { Text("${prop.title} (${prop.address})") },
                            onClick = {
                                selectedPropertyId = prop.id
                                selectedPropertyName = prop.title
                                if (rentText.isBlank()) {
                                    rentText = prop.monthlyRent.toInt().toString()
                                }
                                propertyDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = unitNumber,
                    onValueChange = { unitNumber = it },
                    label = { Text("Unit / House No.") },
                    placeholder = { Text("e.g. A-302") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = rentText,
                    onValueChange = { rentText = it },
                    label = { Text("Agreed Rent (KSh) *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = leaseStart,
                    onValueChange = { leaseStart = it },
                    label = { Text("Lease Start Date") },
                    placeholder = { Text("YYYY-MM-DD") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = leaseEnd,
                    onValueChange = { leaseEnd = it },
                    label = { Text("Lease End Date") },
                    placeholder = { Text("YYYY-MM-DD") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Tenant Status Dropdown
            ExposedDropdownMenuBox(
                expanded = statusDropdownExpanded,
                onExpandedChange = { statusDropdownExpanded = !statusDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = status,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tenancy Status") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusDropdownExpanded) },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                ExposedDropdownMenu(
                    expanded = statusDropdownExpanded,
                    onDismissRequest = { statusDropdownExpanded = false }
                ) {
                    statusOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                status = option
                                statusDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val rent = rentText.toDoubleOrNull() ?: 0.0
                    viewModel.saveTenant(
                        id = existingTenant?.id ?: 0,
                        propertyId = selectedPropertyId,
                        propertyName = selectedPropertyName,
                        fullName = fullName,
                        email = email,
                        phone = phone,
                        unitNumber = unitNumber,
                        rentAmount = rent,
                        leaseStart = leaseStart,
                        leaseEnd = leaseEnd,
                        status = status,
                        onSuccess = onBackClick
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (existingTenant == null) "Register Tenant" else "Update Tenant",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}
