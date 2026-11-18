package com.example.realestatemanagementsystem.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// One row in the "properties" table
@Entity(tableName = "properties")
data class Property(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val location: String,
    val type: String,          // e.g. Apartment, Bedsitter, House
    val monthlyRent: Double
)
