package com.example.mobileappcat2.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tenants")
data class TenantEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val propertyId: Int,
    val propertyName: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val unitNumber: String,
    val rentAmount: Double,
    val leaseStartDate: String,
    val leaseEndDate: String,
    val status: String // Active, Pending, Past
)
