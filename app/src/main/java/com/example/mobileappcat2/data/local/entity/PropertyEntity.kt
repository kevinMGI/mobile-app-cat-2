package com.example.mobileappcat2.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "properties")
data class PropertyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val address: String,
    val propertyType: String, // Apartment, Single Family, Commercial, Villa
    val unitsCount: Int,
    val monthlyRent: Double,
    val status: String, // Occupied, Vacant, Maintenance
    val description: String = ""
)
