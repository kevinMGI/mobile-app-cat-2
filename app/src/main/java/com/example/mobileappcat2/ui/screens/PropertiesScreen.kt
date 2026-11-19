package com.example.mobileappcat2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobileappcat2.ui.components.*
import com.example.mobileappcat2.ui.viewmodel.RealEstateViewModel

@Composable
fun PropertiesScreen(
    viewModel: RealEstateViewModel,
    onPropertyClick: (Int) -> Unit,
    onAddPropertyClick: () -> Unit
) {
    val properties by viewModel.filteredProperties.collectAsState()
    val searchQuery by viewModel.propertySearchQuery.collectAsState()
    val selectedFilter by viewModel.propertyTypeFilter.collectAsState()

    val typeOptions = listOf("All", "Apartment", "Single Family", "Commercial", "Villa")

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddPropertyClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Property")
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
                onQueryChange = { viewModel.propertySearchQuery.value = it },
                placeholder = "Search properties by title or location..."
            )

            Spacer(modifier = Modifier.height(8.dp))
            FilterChipRow(
                options = typeOptions,
                selectedOption = selectedFilter,
                onOptionSelected = { viewModel.propertyTypeFilter.value = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (properties.isEmpty()) {
                EmptyStateComponent(
                    message = "No properties found",
                    subtitle = if (searchQuery.isNotEmpty()) "Try a different search or filter query." else "Start managing real estate by adding your first property.",
                    icon = Icons.Default.HomeWork,
                    onActionClick = onAddPropertyClick,
                    actionLabel = "Add Property"
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(properties) { property ->
                        PropertyCardItem(
                            property = property,
                            onClick = { onPropertyClick(property.id) }
                        )
                    }
                }
            }
        }
    }
}
