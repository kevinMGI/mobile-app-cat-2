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
fun PaymentFormScreen(
    paymentId: Int? = null,
    viewModel: RealEstateViewModel,
    onBackClick: () -> Unit
) {
    val tenants by viewModel.allTenants.collectAsState()
    val payments by viewModel.allPayments.collectAsState()

    val existingPayment = remember(payments, paymentId) {
        payments.find { it.id == paymentId }
    }

    var selectedTenantId by remember(existingPayment, tenants) {
        mutableIntStateOf(existingPayment?.tenantId ?: tenants.firstOrNull()?.id ?: 0)
    }
    var selectedTenantName by remember(existingPayment, tenants) {
        mutableStateOf(existingPayment?.tenantName ?: tenants.firstOrNull()?.fullName ?: "")
    }
    var selectedPropertyName by remember(existingPayment, tenants) {
        mutableStateOf(existingPayment?.propertyName ?: tenants.firstOrNull()?.propertyName ?: "")
    }

    var amountText by remember(existingPayment) { mutableStateOf(existingPayment?.amount?.let { if (it == 0.0) "" else it.toInt().toString() } ?: "") }
    var paymentDate by remember(existingPayment) { mutableStateOf(existingPayment?.paymentDate ?: "2024-11-15") }
    var paymentMethod by remember(existingPayment) { mutableStateOf(existingPayment?.paymentMethod ?: "M-Pesa") }
    var status by remember(existingPayment) { mutableStateOf(existingPayment?.status ?: "Paid") }
    var refNumber by remember(existingPayment) { mutableStateOf(existingPayment?.transactionRef ?: "") }

    val methodOptions = listOf("M-Pesa", "Bank Transfer", "Cash", "Cheque")
    val statusOptions = listOf("Paid", "Pending", "Overdue")

    var tenantDropdownExpanded by remember { mutableStateOf(false) }
    var methodDropdownExpanded by remember { mutableStateOf(false) }
    var statusDropdownExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (paymentId == null) "Record Rent Payment" else "Edit Payment Entry") },
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
                text = "Rent Ledger Transaction",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )

            // Tenant Dropdown
            ExposedDropdownMenuBox(
                expanded = tenantDropdownExpanded,
                onExpandedChange = { tenantDropdownExpanded = !tenantDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = selectedTenantName.ifBlank { "Select Payer Tenant" },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tenant Name *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = tenantDropdownExpanded) },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                ExposedDropdownMenu(
                    expanded = tenantDropdownExpanded,
                    onDismissRequest = { tenantDropdownExpanded = false }
                ) {
                    tenants.forEach { t ->
                        DropdownMenuItem(
                            text = { Text("${t.fullName} (${t.propertyName} - ${t.unitNumber})") },
                            onClick = {
                                selectedTenantId = t.id
                                selectedTenantName = t.fullName
                                selectedPropertyName = "${t.propertyName} (${t.unitNumber})"
                                if (amountText.isBlank()) {
                                    amountText = t.rentAmount.toInt().toString()
                                }
                                tenantDropdownExpanded = false
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
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Amount (KSh) *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = paymentDate,
                    onValueChange = { paymentDate = it },
                    label = { Text("Payment Date") },
                    placeholder = { Text("YYYY-MM-DD") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Payment Method Dropdown
            ExposedDropdownMenuBox(
                expanded = methodDropdownExpanded,
                onExpandedChange = { methodDropdownExpanded = !methodDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = paymentMethod,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Payment Method") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = methodDropdownExpanded) },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                ExposedDropdownMenu(
                    expanded = methodDropdownExpanded,
                    onDismissRequest = { methodDropdownExpanded = false }
                ) {
                    methodOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                paymentMethod = option
                                methodDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = refNumber,
                onValueChange = { refNumber = it },
                label = { Text("Transaction Ref / Receipt No.") },
                placeholder = { Text("e.g. QWE987654321") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // Status Dropdown
            ExposedDropdownMenuBox(
                expanded = statusDropdownExpanded,
                onExpandedChange = { statusDropdownExpanded = !statusDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = status,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Payment Status") },
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
                    val amt = amountText.toDoubleOrNull() ?: 0.0
                    viewModel.savePayment(
                        id = existingPayment?.id ?: 0,
                        tenantId = selectedTenantId,
                        tenantName = selectedTenantName,
                        propertyName = selectedPropertyName,
                        amount = amt,
                        paymentDate = paymentDate,
                        paymentMethod = paymentMethod,
                        status = status,
                        ref = refNumber,
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
                    text = if (existingPayment == null) "Record Payment" else "Update Payment",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}
