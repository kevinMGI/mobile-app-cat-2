package com.example.mobileappcat2.data.local.dao

import androidx.room.*
import com.example.mobileappcat2.data.local.entity.TenantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TenantDao {
    @Query("SELECT * FROM tenants ORDER BY id DESC")
    fun getAllTenants(): Flow<List<TenantEntity>>

    @Query("SELECT * FROM tenants WHERE id = :id")
    suspend fun getTenantById(id: Int): TenantEntity?

    @Query("SELECT * FROM tenants WHERE propertyId = :propertyId")
    fun getTenantsForProperty(propertyId: Int): Flow<List<TenantEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTenant(tenant: TenantEntity): Long

    @Update
    suspend fun updateTenant(tenant: TenantEntity)

    @Delete
    suspend fun deleteTenant(tenant: TenantEntity)

    @Query("DELETE FROM tenants WHERE id = :id")
    suspend fun deleteTenantById(id: Int)
}
