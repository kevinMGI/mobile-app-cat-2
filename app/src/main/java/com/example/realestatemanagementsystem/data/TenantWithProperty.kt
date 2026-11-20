package com.example.realestatemanagementsystem.data

import androidx.room.Embedded
import androidx.room.Relation

// A tenant together with the property they rent (used for list screens)
data class TenantWithProperty(
    @Embedded val tenant: Tenant,
    @Relation(parentColumn = "propertyId", entityColumn = "id")
    val property: Property?
)
