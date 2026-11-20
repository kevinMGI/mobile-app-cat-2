package com.example.realestatemanagementsystem.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TenantDao {

    @Insert
    suspend fun insert(tenant: Tenant): Long

    @Update
    suspend fun update(tenant: Tenant)

    @Delete
    suspend fun delete(tenant: Tenant)

    @Query("SELECT * FROM tenants ORDER BY name ASC")
    fun getAll(): Flow<List<Tenant>>

    @Query("SELECT * FROM tenants WHERE id = :id")
    suspend fun getById(id: Long): Tenant?

    @Query("SELECT * FROM tenants WHERE propertyId = :propertyId")
    fun getByProperty(propertyId: Long): Flow<List<Tenant>>

    @Transaction
    @Query("SELECT * FROM tenants ORDER BY name ASC")
    fun getAllWithProperty(): Flow<List<TenantWithProperty>>
}
