package com.example.realestatemanagementsystem.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// One row in the "tenants" table. propertyId links the tenant to a Property.
@Entity(
    tableName = "tenants",
    foreignKeys = [ForeignKey(
        entity = Property::class,
        parentColumns = ["id"],
        childColumns = ["propertyId"],
        onDelete = ForeignKey.SET_NULL   // if a property is deleted, the tenant stays but is unassigned
    )],
    indices = [Index("propertyId")]
)
data class Tenant(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val phone: String,
    val email: String,
    val propertyId: Long? = null,
    val moveInDate: Long = System.currentTimeMillis()
)
