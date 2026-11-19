package com.example.mobileappcat2.data.local.dao

import androidx.room.*
import com.example.mobileappcat2.data.local.entity.PaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDao {
    @Query("SELECT * FROM payments ORDER BY id DESC")
    fun getAllPayments(): Flow<List<PaymentEntity>>

    @Query("SELECT * FROM payments WHERE id = :id")
    suspend fun getPaymentById(id: Int): PaymentEntity?

    @Query("SELECT * FROM payments WHERE tenantId = :tenantId")
    fun getPaymentsForTenant(tenantId: Int): Flow<List<PaymentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: PaymentEntity): Long

    @Update
    suspend fun updatePayment(payment: PaymentEntity)

    @Delete
    suspend fun deletePayment(payment: PaymentEntity)

    @Query("DELETE FROM payments WHERE id = :id")
    suspend fun deletePaymentById(id: Int)
}
