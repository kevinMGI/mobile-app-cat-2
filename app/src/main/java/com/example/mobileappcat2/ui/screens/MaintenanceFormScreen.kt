package com.example.mobileappcat2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mobileappcat2.ui.components.ConfirmDeleteDialog
import com.example.mobileappcat2.ui.viewmodel.RealEstateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaintenanceFormScreen(
    requestId: Int? = null,
    viewModel: RealEstateViewModel,
    onBackClick: () -> Unit
) {
    val properties by viewModel.allProperties.collectAsState()
    val tenants by viewModel.allTenants.collectAsState()
    val requests by viewModel.allMaintenanceRequests.collectAsState()

    val existingRequest = remember(requests, requestId) {
        requests.find { it.id == requestId }
    }

    var selectedPropertyId by remember(existingRequest, properties) {
        mutableIntStateOf(existingRequest?.propertyId ?: properties.firstOrNull()?.id ?: 0)
    }
    var selectedPropertyName by remember(existingRequest, properties) {
        mutableStateOf(existingRequest?.propertyName ?: properties.firstOrNull()?.title ?: "")
    }
    var selectedTenantName by remember(existingRequest, tenants) {
        mutableStateOf(existingRequest?.tenantName ?: tenants.firstOrNull()?.fullName ?: "")
    }

    var title by remember(existingRequest) { mutableStateOf(existingRequest?.title ?: "") }
    var category by remember(existingRequest) { mutableStateOf(existingRequest?.category ?: "Plumbing") }
    var priority by remember(existingRequest) { mutableStateOf(existingRequest?.priority ?: "Medium") }
    var status by remember(existingRequest) { mutableStateOf(existingRequest?.status ?: "Pending") }
    var dateReported by remember(existingRequest) { mutableStateOf(existingRequest?.dateReported ?: "2024-11-15") }
    var description by remember(existingRequest) { mutableStateOf(existingRequest?.description ?: "") }

    var showDeleteDialog by remember { mutableStateOf(false) }

    val categoryOptions = listOf("Plumbing", "Electrical", "HVAC", "Appliance", "Structural")
    val priorityOptions = listOf("High", "Medium", "Low")
    val statusOptions = listOf("Pending", "In Progress", "Resolved")

    var propertyDropdownExpanded by remember { mutableStateOf(false) }
    var categoryDropdownExpanded by remember { mutableStateOf(false) }
    var priorityDropdownExpanded by remember { mutableStateOf(false) }
    var statusDropdownExpanded by remember { mutableStateOf(false) }

    if (showDeleteDialog && existingRequest != null) {
        ConfirmDeleteDialog(
            title = "Resolve & Delete Ticket",
            message = "Are you sure you want to delete this maintenance request?",
            onConfirm = {
                viewModel.deleteMaintenanceRequest(existingRequest)
                showDeleteDialog = false
                onBackClick()
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (requestId == null) "Log Maintenance Request" else "Maintenance Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (existingRequest != null) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Request", tint = MaterialTheme.colorScheme.error)
                        }
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
                text = "Maintenance Ticket Form",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Issue Title *") },
                placeholder = { Text("e.g. Water leak under kitchen sink") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // Property Selection Dropdown
            ExposedDropdownMenuBox(
                expanded = propertyDropdownExpanded,
                onExpandedChange = { propertyDropdownExpanded = !propertyDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = selectedPropertyName.ifBlank { "Select Property" },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Property *") },
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
                            text = { Text(prop.title) },
                            onClick = {
                                selectedPropertyId = prop.id
                                selectedPropertyName = prop.title
                                propertyDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = selectedTenantName,
                onValueChange = { selectedTenantName = it },
                label = { Text("Reported By / Tenant") },
                placeholder = { Text("e.g. Jane Wanjiru (A-302)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Category Dropdown
                ExposedDropdownMenuBox(
                    expanded = categoryDropdownExpanded,
                    onExpandedChange = { categoryDropdownExpanded = !categoryDropdownExpanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropdownExpanded) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = categoryDropdownExpanded,
                        onDismissRequest = { categoryDropdownExpanded = false }
                    ) {
                        categoryOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    category = option
                                    categoryDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Priority Dropdown
                ExposedDropdownMenuBox(
                    expanded = priorityDropdownExpanded,
                    onExpandedChange = { priorityDropdownExpanded = !priorityDropdownExpanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = priority,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Priority") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = priorityDropdownExpanded) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = priorityDropdownExpanded,
                        onDismissRequest = { priorityDropdownExpanded = false }
                    ) {
                        priorityOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    priority = option
                                    priorityDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Status & Date Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = statusDropdownExpanded,
                    onExpandedChange = { statusDropdownExpanded = !statusDropdownExpanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = status,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Ticket Status") },
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

                OutlinedTextField(
                    value = dateReported,
                    onValueChange = { dateReported = it },
                    label = { Text("Date Reported") },
                    placeholder = { Text("YYYY-MM-DD") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Issue Details & Description") },
                placeholder = { Text("Provide details about the issue and work required...") },
                minLines = 3,
                maxLines = 5,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.saveMaintenanceRequest(
                        id = existingRequest?.id ?: 0,
                        propertyId = selectedPropertyId,
                        propertyName = selectedPropertyName,
                        tenantName = selectedTenantName,
                        title = title,
                        category = category,
                        priority = priority,
                        status = status,
                        dateReported = dateReported,
                        description = description,
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
                    text = if (existingRequest == null) "Log Request" else "Update Ticket",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}
