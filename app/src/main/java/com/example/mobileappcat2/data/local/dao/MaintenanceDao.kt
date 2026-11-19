package com.example.mobileappcat2.data.local.dao

import androidx.room.*
import com.example.mobileappcat2.data.local.entity.MaintenanceRequestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MaintenanceDao {
    @Query("SELECT * FROM maintenance_requests ORDER BY id DESC")
    fun getAllRequests(): Flow<List<MaintenanceRequestEntity>>

    @Query("SELECT * FROM maintenance_requests WHERE id = :id")
    suspend fun getRequestById(id: Int): MaintenanceRequestEntity?

    @Query("SELECT * FROM maintenance_requests WHERE propertyId = :propertyId")
    fun getRequestsForProperty(propertyId: Int): Flow<List<MaintenanceRequestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequest(request: MaintenanceRequestEntity): Long

    @Update
    suspend fun updateRequest(request: MaintenanceRequestEntity)

    @Delete
    suspend fun deleteRequest(request: MaintenanceRequestEntity)

    @Query("DELETE FROM maintenance_requests WHERE id = :id")
    suspend fun deleteRequestById(id: Int)
}
