package com.example.realestatemanagementsystem.data

import android.content.Context
import kotlinx.coroutines.flow.Flow

// The single place the UI talks to for data. Screens never call the DAOs directly.
class RealEstateRepository(
    private val propertyDao: PropertyDao,
    private val tenantDao: TenantDao
) {
    // Read (these update automatically when the data changes)
    val properties: Flow<List<Property>> = propertyDao.getAll()
    val tenants: Flow<List<Tenant>> = tenantDao.getAll()
    val tenantsWithProperty: Flow<List<TenantWithProperty>> = tenantDao.getAllWithProperty()

    // Properties: create / read one / update / delete
    suspend fun addProperty(property: Property): Long = propertyDao.insert(property)
    suspend fun getProperty(id: Long): Property? = propertyDao.getById(id)
    suspend fun updateProperty(property: Property) = propertyDao.update(property)
    suspend fun deleteProperty(property: Property) = propertyDao.delete(property)

    // Tenants: create / read one / update / delete
    suspend fun addTenant(tenant: Tenant): Long = tenantDao.insert(tenant)
    suspend fun getTenant(id: Long): Tenant? = tenantDao.getById(id)
    suspend fun updateTenant(tenant: Tenant) = tenantDao.update(tenant)
    suspend fun deleteTenant(tenant: Tenant) = tenantDao.delete(tenant)
    fun tenantsForProperty(propertyId: Long): Flow<List<Tenant>> = tenantDao.getByProperty(propertyId)

    companion object {
        fun create(context: Context): RealEstateRepository {
            val db = AppDatabase.getInstance(context)
            return RealEstateRepository(db.propertyDao(), db.tenantDao())
        }
    }
}
