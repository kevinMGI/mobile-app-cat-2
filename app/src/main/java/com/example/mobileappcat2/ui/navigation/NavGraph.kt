package com.example.mobileappcat2.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.mobileappcat2.ui.screens.*
import com.example.mobileappcat2.ui.viewmodel.RealEstateViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(viewModel: RealEstateViewModel) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Listen for snackbar messages from ViewModel
    LaunchedEffect(key1 = true) {
        viewModel.snackbarMessage.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    val isTopLevelDestination = bottomNavItems.any { it.route == currentRoute }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (currentRoute) {
                            Screen.Dashboard.route -> "RentPulse Dashboard"
                            Screen.PropertyList.route -> "Properties"
                            Screen.TenantList.route -> "Tenants Management"
                            Screen.PaymentList.route -> "Rent Ledger"
                            Screen.MaintenanceList.route -> "Maintenance Tickets"
                            Screen.Settings.route -> "App Overview & Setup"
                            else -> "RentPulse Real Estate"
                        },
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                navigationIcon = {
                    if (!isTopLevelDestination) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                actions = {
                    if (isTopLevelDestination && currentRoute != Screen.Settings.route) {
                        IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = "Info & Setup",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (isTopLevelDestination) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = MaterialTheme.colorScheme.surfaceTint.let { 8.toDp() }
                ) {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { screen.icon?.let { Icon(it, contentDescription = screen.title) } },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    viewModel = viewModel,
                    onNavigateToProperties = { navController.navigate(Screen.PropertyList.route) },
                    onNavigateToTenants = { navController.navigate(Screen.TenantList.route) },
                    onNavigateToPayments = { navController.navigate(Screen.PaymentList.route) },
                    onNavigateToMaintenance = { navController.navigate(Screen.MaintenanceList.route) },
                    onNavigateToPropertyForm = { navController.navigate(Screen.PropertyForm.createRoute()) },
                    onNavigateToTenantForm = { navController.navigate(Screen.TenantForm.createRoute()) }
                )
            }

            composable(Screen.PropertyList.route) {
                PropertiesScreen(
                    viewModel = viewModel,
                    onPropertyClick = { propId ->
                        navController.navigate(Screen.PropertyDetail.createRoute(propId))
                    },
                    onAddPropertyClick = {
                        navController.navigate(Screen.PropertyForm.createRoute())
                    }
                )
            }

            composable(
                route = Screen.PropertyDetail.route,
                arguments = listOf(navArgument("propertyId") { type = NavType.IntType })
            ) { backStackEntry ->
                val propId = backStackEntry.arguments?.getInt("propertyId") ?: 0
                PropertyDetailScreen(
                    propertyId = propId,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    onEditClick = { id ->
                        navController.navigate(Screen.PropertyForm.createRoute(id))
                    }
                )
            }

            composable(
                route = Screen.PropertyForm.route,
                arguments = listOf(navArgument("propertyId") {
                    type = NavType.IntType
                    defaultValue = -1
                })
            ) { backStackEntry ->
                val propId = backStackEntry.arguments?.getInt("propertyId").let {
                    if (it == null || it == -1) null else it
                }
                PropertyFormScreen(
                    propertyId = propId,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Screen.TenantList.route) {
                TenantsScreen(
                    viewModel = viewModel,
                    onTenantClick = { tenantId ->
                        navController.navigate(Screen.TenantDetail.createRoute(tenantId))
                    },
                    onAddTenantClick = {
                        navController.navigate(Screen.TenantForm.createRoute())
                    }
                )
            }

            composable(
                route = Screen.TenantDetail.route,
                arguments = listOf(navArgument("tenantId") { type = NavType.IntType })
            ) { backStackEntry ->
                val tenantId = backStackEntry.arguments?.getInt("tenantId") ?: 0
                TenantDetailScreen(
                    tenantId = tenantId,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    onEditClick = { id ->
                        navController.navigate(Screen.TenantForm.createRoute(id))
                    }
                )
            }

            composable(
                route = Screen.TenantForm.route,
                arguments = listOf(navArgument("tenantId") {
                    type = NavType.IntType
                    defaultValue = -1
                })
            ) { backStackEntry ->
                val tenantId = backStackEntry.arguments?.getInt("tenantId").let {
                    if (it == null || it == -1) null else it
                }
                TenantFormScreen(
                    tenantId = tenantId,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Screen.PaymentList.route) {
                PaymentsScreen(
                    viewModel = viewModel,
                    onRecordPaymentClick = {
                        navController.navigate(Screen.PaymentForm.createRoute())
                    }
                )
            }

            composable(
                route = Screen.PaymentForm.route,
                arguments = listOf(navArgument("paymentId") {
                    type = NavType.IntType
                    defaultValue = -1
                })
            ) { backStackEntry ->
                val paymentId = backStackEntry.arguments?.getInt("paymentId").let {
                    if (it == null || it == -1) null else it
                }
                PaymentFormScreen(
                    paymentId = paymentId,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Screen.MaintenanceList.route) {
                MaintenanceScreen(
                    viewModel = viewModel,
                    onAddTicketClick = {
                        navController.navigate(Screen.MaintenanceForm.createRoute())
                    },
                    onTicketClick = { reqId ->
                        navController.navigate(Screen.MaintenanceForm.createRoute(reqId))
                    }
                )
            }

            composable(
                route = Screen.MaintenanceForm.route,
                arguments = listOf(navArgument("requestId") {
                    type = NavType.IntType
                    defaultValue = -1
                })
            ) { backStackEntry ->
                val requestId = backStackEntry.arguments?.getInt("requestId").let {
                    if (it == null || it == -1) null else it
                }
                MaintenanceFormScreen(
                    requestId = requestId,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen(viewModel = viewModel)
            }
        }
    }
}

// Helper extension to convert Int to Dp safely
private fun Int.toDp(): Dp = Dp(this.toFloat())
