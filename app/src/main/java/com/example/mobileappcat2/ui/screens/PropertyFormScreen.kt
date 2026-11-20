package com.example.mobileappcat2.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mobileappcat2.ui.viewmodel.RealEstateViewModel
import com.example.mobileappcat2.util.InputValidator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyFormScreen(
    propertyId: Int? = null,
    viewModel: RealEstateViewModel,
    onBackClick: () -> Unit
) {
    val properties by viewModel.allProperties.collectAsState()

    val existingProperty = remember(properties, propertyId) {
        properties.find { it.id == propertyId }
    }

    var title by remember(existingProperty) {
        mutableStateOf(existingProperty?.title ?: "")
    }

    var titleError by remember { mutableStateOf<String?>(null) }
    var addressError by remember { mutableStateOf<String?>(null) }
    var rentError by remember { mutableStateOf<String?>(null) }

    val typeOptions = listOf("Apartment", "Single Family", "Commercial", "Villa")
    val statusOptions = listOf("Occupied", "Vacant", "Maintenance")

    var typeDropdownExpanded by remember { mutableStateOf(false) }
    var statusDropdownExpanded by remember { mutableStateOf(false) }
    var hasAttemptedSave by remember { mutableStateOf(false) }

    val invalidUnits = unitsCountText.toIntOrNull()?.let {
        it <= 0
    } ?: true

    val invalidRent = rentText.toDoubleOrNull()?.let {
        it <= 0 || !it.isFinite()
    } ?: true

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (propertyId == null) "Add New Property"
                        else "Edit Property"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
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
                text = "Property Information",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            // Property Title
            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    if (titleError != null) titleError = InputValidator.validatePropertyName(it).errorMessageOrNull
                },
                label = { Text("Property Title *") },
                placeholder = { Text("e.g. Sunset Heights Apartments") },
                isError = titleError != null,
                supportingText = { titleError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                singleLine = true,
                isError = hasAttemptedSave && title.isBlank(),
                supportingText = {
                    if (hasAttemptedSave && title.isBlank()) {
                        Text("Property title is required")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // Property Address
            OutlinedTextField(
                value = address,
                onValueChange = {
                    address = it
                    if (addressError != null) addressError = InputValidator.validateAddress(it).errorMessageOrNull
                },
                label = { Text("Property Address / Location *") },
                placeholder = { Text("e.g. 124 Parklands Avenue, Nairobi") },
                isError = addressError != null,
                supportingText = { addressError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                singleLine = true,
                isError = hasAttemptedSave && address.isBlank(),
                supportingText = {
                    if (hasAttemptedSave && address.isBlank()) {
                        Text("Property address is required")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            ExposedDropdownMenuBox(
                expanded = typeDropdownExpanded,
                onExpandedChange = {
                    typeDropdownExpanded = !typeDropdownExpanded
                }
            ) {
                OutlinedTextField(
                    value = propertyType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Property Type") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = typeDropdownExpanded
                        )
                    },
                    modifier = Modifier
                        .menuAnchor(
                            MenuAnchorType.PrimaryNotEditable,
                            true
                        )
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                ExposedDropdownMenu(
                    expanded = typeDropdownExpanded,
                    onDismissRequest = {
                        typeDropdownExpanded = false
                    }
                ) {
                    typeOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                propertyType = option
                                typeDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            // Units and Monthly Rent
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = unitsCountText,
                    onValueChange = { unitsCountText = it },
                    label = { Text("Number of Units") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    isError = hasAttemptedSave && invalidUnits,
                    supportingText = {
                        if (hasAttemptedSave && invalidUnits) {
                            Text("Enter a valid number greater than zero")
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = rentText,
                    onValueChange = {
                        rentText = it
                        if (rentError != null) rentError = InputValidator.validateRentAmount(it).errorMessageOrNull
                    },
                    label = { Text("Monthly Rent (KSh) *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = rentError != null,
                    supportingText = { rentError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            ExposedDropdownMenuBox(
                expanded = statusDropdownExpanded,
                onExpandedChange = {
                    statusDropdownExpanded = !statusDropdownExpanded
                }
            ) {
                OutlinedTextField(
                    value = status,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Property Status") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = statusDropdownExpanded
                        )
                    },
                    modifier = Modifier
                        .menuAnchor(
                            MenuAnchorType.PrimaryNotEditable,
                            true
                        )
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                ExposedDropdownMenu(
                    expanded = statusDropdownExpanded,
                    onDismissRequest = {
                        statusDropdownExpanded = false
                    }
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

            // Description
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description & Notes") },
                placeholder = {
                    Text(
                        "Enter details about amenities, parking, security, etc."
                    )
                },
                minLines = 3,
                maxLines = 5,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Save or Update Property
            Button(
                onClick = {
                    val tRes = InputValidator.validatePropertyName(title)
                    val aRes = InputValidator.validateAddress(address)
                    val rRes = InputValidator.validateRentAmount(rentText)

                    titleError = tRes.errorMessageOrNull
                    addressError = aRes.errorMessageOrNull
                    rentError = rRes.errorMessageOrNull

                    if (tRes.isValid && aRes.isValid && rRes.isValid) {
                        val units = unitsCountText.toIntOrNull() ?: 1
                        val rent = rentText.toDoubleOrNull() ?: 0.0
                        viewModel.saveProperty(
                            id = existingProperty?.id ?: 0,
                            title = title,
                            address = address,
                            type = propertyType,
                            units = units,
                            rent = rent,
                            status = status,
                            description = description,
                            onSuccess = onBackClick
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = if (existingProperty == null) {
                        "Save Property"
                    } else {
                        "Update Property"
                    },
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}
