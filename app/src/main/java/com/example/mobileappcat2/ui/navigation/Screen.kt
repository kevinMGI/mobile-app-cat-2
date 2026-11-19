package com.example.mobileappcat2.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Dashboard)
    object PropertyList : Screen("property_list", "Properties", Icons.Default.HomeWork)
    object PropertyDetail : Screen("property_detail/{propertyId}", "Property Detail") {
        fun createRoute(propertyId: Int) = "property_detail/$propertyId"
    }
    object PropertyForm : Screen("property_form?propertyId={propertyId}", "Property Form") {
        fun createRoute(propertyId: Int? = null) = if (propertyId != null) "property_form?propertyId=$propertyId" else "property_form"
    }

    object TenantList : Screen("tenant_list", "Tenants", Icons.Default.People)
    object TenantDetail : Screen("tenant_detail/{tenantId}", "Tenant Detail") {
        fun createRoute(tenantId: Int) = "tenant_detail/$tenantId"
    }
    object TenantForm : Screen("tenant_form?tenantId={tenantId}", "Tenant Form") {
        fun createRoute(tenantId: Int? = null) = if (tenantId != null) "tenant_form?tenantId=$tenantId" else "tenant_form"
    }

    object PaymentList : Screen("payment_list", "Payments", Icons.Default.Payments)
    object PaymentForm : Screen("payment_form?paymentId={paymentId}", "Record Payment") {
        fun createRoute(paymentId: Int? = null) = if (paymentId != null) "payment_form?paymentId=$paymentId" else "payment_form"
    }

    object MaintenanceList : Screen("maintenance_list", "Maintenance", Icons.Default.Build)
    object MaintenanceForm : Screen("maintenance_form?requestId={requestId}", "Maintenance Ticket") {
        fun createRoute(requestId: Int? = null) = if (requestId != null) "maintenance_form?requestId=$requestId" else "maintenance_form"
    }

    object Settings : Screen("settings", "Info & Setup", Icons.Default.Settings)
}

val bottomNavItems = listOf(
    Screen.Dashboard,
    Screen.PropertyList,
    Screen.TenantList,
    Screen.PaymentList,
    Screen.MaintenanceList
)
