package com.example.mobileappcat2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mobileappcat2.ui.components.*
import com.example.mobileappcat2.ui.viewmodel.RealEstateViewModel

@Composable
fun DashboardScreen(
    viewModel: RealEstateViewModel,
    onNavigateToProperties: () -> Unit,
    onNavigateToTenants: () -> Unit,
    onNavigateToPayments: () -> Unit,
    onNavigateToMaintenance: () -> Unit,
    onNavigateToPropertyForm: () -> Unit,
    onNavigateToTenantForm: () -> Unit
) {
    val properties by viewModel.allProperties.collectAsState()
    val tenants by viewModel.allTenants.collectAsState()
    val payments by viewModel.allPayments.collectAsState()
    val maintenanceRequests by viewModel.allMaintenanceRequests.collectAsState()
    val totalRev by viewModel.totalRevenue.collectAsState()
    val pendingRev by viewModel.pendingRevenue.collectAsState()
    val activeMaintCount by viewModel.activeMaintenanceCount.collectAsState()

    val totalPropertiesCount = properties.size
    val occupiedPropertiesCount = properties.count { it.status == "Occupied" }
    val occupancyRate = if (totalPropertiesCount > 0) (occupiedPropertiesCount.toFloat() / totalPropertiesCount.toFloat()) else 0f

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Header Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "RentPulse Management",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Property Overview",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Track properties, active leases, monthly rent collection, and maintenance in real-time.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
                    )
                }
            }
        }

        // Metrics Grid (2x2)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard(
                        title = "Total Revenue",
                        value = "KSh ${String.format("%,.0f", totalRev)}",
                        icon = Icons.Default.Payments,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Pending Rent",
                        value = "KSh ${String.format("%,.0f", pendingRev)}",
                        icon = Icons.Default.Schedule,
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard(
                        title = "Properties",
                        value = "$totalPropertiesCount Listed",
                        icon = Icons.Default.HomeWork,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Active Maintenance",
                        value = "$activeMaintCount Issues",
                        icon = Icons.Default.Build,
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Occupancy Rate Bar Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Portfolio Occupancy",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${(occupancyRate * 100).toInt()}% Occupied",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { occupancyRate },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "$occupiedPropertiesCount of $totalPropertiesCount units occupied",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }

        // Quick Action Shortcuts
        item {
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onNavigateToPropertyForm,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.AddHome, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Property")
                }
                Button(
                    onClick = onNavigateToTenantForm,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Tenant")
                }
            }
        }

        // Recent Maintenance Tickets Section Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Maintenance Requests",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                TextButton(onClick = onNavigateToMaintenance) {
                    Text("View All")
                }
            }
        }

        if (maintenanceRequests.isEmpty()) {
            item {
                EmptyStateComponent(
                    message = "No maintenance issues reported",
                    subtitle = "All properties are in excellent condition."
                )
            }
        } else {
            items(maintenanceRequests.take(3)) { req ->
                MaintenanceCardItem(
                    request = req,
                    onClick = onNavigateToMaintenance
                )
            }
        }
    }
}
