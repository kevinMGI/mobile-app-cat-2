package com.example.mobileappcat2.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payments")
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tenantId: Int,
    val tenantName: String,
    val propertyName: String,
    val amount: Double,
    val paymentDate: String,
    val paymentMethod: String, // Bank Transfer, M-Pesa, Cash, Cheque
    val status: String, // Paid, Pending, Overdue
    val transactionRef: String
)
