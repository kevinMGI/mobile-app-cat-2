package com.example.mobileappcat2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobileappcat2.ui.components.*
import com.example.mobileappcat2.ui.viewmodel.RealEstateViewModel

@Composable
fun MaintenanceScreen(
    viewModel: RealEstateViewModel,
    onAddTicketClick: () -> Unit,
    onTicketClick: (Int) -> Unit
) {
    val requests by viewModel.filteredMaintenanceRequests.collectAsState()
    val searchQuery by viewModel.maintenanceSearchQuery.collectAsState()
    val selectedFilter by viewModel.maintenanceStatusFilter.collectAsState()

    val statusOptions = listOf("All", "Pending", "In Progress", "Resolved")

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTicketClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Log Maintenance Request")
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
            SearchBarComponent(
                query = searchQuery,
                onQueryChange = { viewModel.maintenanceSearchQuery.value = it },
                placeholder = "Search issues, property, or tenant..."
            )

            Spacer(modifier = Modifier.height(8.dp))
            FilterChipRow(
                options = statusOptions,
                selectedOption = selectedFilter,
                onOptionSelected = { viewModel.maintenanceStatusFilter.value = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (requests.isEmpty()) {
                EmptyStateComponent(
                    message = "No maintenance tickets found",
                    subtitle = if (searchQuery.isNotEmpty()) "No tickets match your query." else "Report tenant repair requests, plumbing issues, or electrical tasks.",
                    icon = Icons.Default.Build,
                    onActionClick = onAddTicketClick,
                    actionLabel = "Report Issue"
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(requests) { req ->
                        MaintenanceCardItem(
                            request = req,
                            onClick = { onTicketClick(req.id) }
                        )
                    }
                }
            }
        }
    }
}
