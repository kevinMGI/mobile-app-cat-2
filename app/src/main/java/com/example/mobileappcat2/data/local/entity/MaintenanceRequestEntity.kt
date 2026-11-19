package com.example.mobileappcat2.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "maintenance_requests")
data class MaintenanceRequestEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val propertyId: Int,
    val propertyName: String,
    val tenantName: String,
    val title: String,
    val category: String, // Plumbing, Electrical, HVAC, Appliance, Structural
    val priority: String, // High, Medium, Low
    val status: String, // Pending, In Progress, Resolved
    val dateReported: String,
    val description: String
)
