package com.example.mobileappcat2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mobileappcat2.ui.components.*
import com.example.mobileappcat2.ui.viewmodel.RealEstateViewModel

@Composable
fun PaymentsScreen(
    viewModel: RealEstateViewModel,
    onRecordPaymentClick: () -> Unit
) {
    val payments by viewModel.filteredPayments.collectAsState()
    val searchQuery by viewModel.paymentSearchQuery.collectAsState()
    val selectedFilter by viewModel.paymentStatusFilter.collectAsState()

    val totalCollected by viewModel.totalRevenue.collectAsState()
    val pendingTotal by viewModel.pendingRevenue.collectAsState()

    val statusOptions = listOf("All", "Paid", "Pending", "Overdue")

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onRecordPaymentClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Record Payment")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Revenue Summary Banner Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Total Rent Collected", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.outline)
                        Text(
                            "KSh ${String.format("%,.0f", totalCollected)}",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("Pending Balance", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.outline)
                        Text(
                            "KSh ${String.format("%,.0f", pendingTotal)}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            SearchBarComponent(
                query = searchQuery,
                onQueryChange = { viewModel.paymentSearchQuery.value = it },
                placeholder = "Search payment ref, tenant, or property..."
            )

            Spacer(modifier = Modifier.height(8.dp))
            FilterChipRow(
                options = statusOptions,
                selectedOption = selectedFilter,
                onOptionSelected = { viewModel.paymentStatusFilter.value = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (payments.isEmpty()) {
                EmptyStateComponent(
                    message = "No payment records found",
                    subtitle = if (searchQuery.isNotEmpty()) "No transactions match your search filter." else "Log tenant rent payments to maintain financial ledger history.",
                    icon = Icons.Default.Payments,
                    onActionClick = onRecordPaymentClick,
                    actionLabel = "Record Payment"
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(payments) { payment ->
                        PaymentCardItem(
                            payment = payment,
                            onClick = { }
                        )
                    }
                }
            }
        }
    }
}
