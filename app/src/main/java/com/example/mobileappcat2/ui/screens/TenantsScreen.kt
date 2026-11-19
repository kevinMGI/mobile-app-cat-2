package com.example.mobileappcat2.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mobileappcat2.ui.components.*
import com.example.mobileappcat2.ui.viewmodel.RealEstateViewModel

@Composable
fun TenantsScreen(
    viewModel: RealEstateViewModel,
    onTenantClick: (Int) -> Unit,
    onAddTenantClick: () -> Unit
) {
    val context = LocalContext.current
    val tenants by viewModel.filteredTenants.collectAsState()
    val searchQuery by viewModel.tenantSearchQuery.collectAsState()
    val selectedFilter by viewModel.tenantStatusFilter.collectAsState()

    val statusOptions = listOf("All", "Active", "Pending", "Past")

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTenantClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Tenant")
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
                onQueryChange = { viewModel.tenantSearchQuery.value = it },
                placeholder = "Search tenant name, property, or unit..."
            )

            Spacer(modifier = Modifier.height(8.dp))
            FilterChipRow(
                options = statusOptions,
                selectedOption = selectedFilter,
                onOptionSelected = { viewModel.tenantStatusFilter.value = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (tenants.isEmpty()) {
                EmptyStateComponent(
                    message = "No tenants found",
                    subtitle = if (searchQuery.isNotEmpty()) "Try adjusting your search criteria." else "Add tenants to keep track of rent payments and lease agreements.",
                    icon = Icons.Default.People,
                    onActionClick = onAddTenantClick,
                    actionLabel = "Add Tenant"
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(tenants) { tenant ->
                        TenantCardItem(
                            tenant = tenant,
                            onClick = { onTenantClick(tenant.id) },
                            onCallClick = {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:${tenant.phone}")
                                }
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }
}
