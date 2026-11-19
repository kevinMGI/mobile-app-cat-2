package com.example.mobileappcat2.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mobileappcat2.data.local.dao.MaintenanceDao
import com.example.mobileappcat2.data.local.dao.PaymentDao
import com.example.mobileappcat2.data.local.dao.PropertyDao
import com.example.mobileappcat2.data.local.dao.TenantDao
import com.example.mobileappcat2.data.local.entity.MaintenanceRequestEntity
import com.example.mobileappcat2.data.local.entity.PaymentEntity
import com.example.mobileappcat2.data.local.entity.PropertyEntity
import com.example.mobileappcat2.data.local.entity.TenantEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        PropertyEntity::class,
        TenantEntity::class,
        PaymentEntity::class,
        MaintenanceRequestEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun propertyDao(): PropertyDao
    abstract fun tenantDao(): TenantDao
    abstract fun paymentDao(): PaymentDao
    abstract fun maintenanceDao(): MaintenanceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rentpulse_database"
                )
                .addCallback(DatabaseCallback())
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateSeedData(database)
                    }
                }
            }
        }

        suspend fun populateSeedData(db: AppDatabase) {
            val propDao = db.propertyDao()
            val tenantDao = db.tenantDao()
            val payDao = db.paymentDao()
            val maintDao = db.maintenanceDao()

            // Seed Properties
            val prop1Id = propDao.insertProperty(
                PropertyEntity(
                    title = "Sunset Heights Apartments",
                    address = "124 Parklands Avenue, Nairobi",
                    propertyType = "Apartment",
                    unitsCount = 12,
                    monthlyRent = 45000.0,
                    status = "Occupied",
                    description = "Modern 2-bedroom apartments with balcony, 24/7 security, backup generator, and high-speed internet."
                )
            ).toInt()

            val prop2Id = propDao.insertProperty(
                PropertyEntity(
                    title = "Grandview Townhouses",
                    address = "45 Karen Road, Nairobi",
                    propertyType = "Single Family",
                    unitsCount = 4,
                    monthlyRent = 120000.0,
                    status = "Occupied",
                    description = "Luxury 4-bedroom townhouses with private garden, DSQ, perimeter fence, and double garage."
                )
            ).toInt()

            val prop3Id = propDao.insertProperty(
                PropertyEntity(
                    title = "Westlands Commercial Plaza",
                    address = "88 Ring Road Westlands, Nairobi",
                    propertyType = "Commercial",
                    unitsCount = 8,
                    monthlyRent = 85000.0,
                    status = "Occupied",
                    description = "Prime office spaces suitable for tech startups, law firms, and consulting agencies."
                )
            ).toInt()

            val prop4Id = propDao.insertProperty(
                PropertyEntity(
                    title = "Kilimani Heights Villa",
                    address = "12 Lenana Road, Nairobi",
                    propertyType = "Villa",
                    unitsCount = 2,
                    monthlyRent = 150000.0,
                    status = "Vacant",
                    description = "Spacious 5-bedroom executive villa with swimming pool and mature gardens."
                )
            ).toInt()

            // Seed Tenants
            val t1Id = tenantDao.insertTenant(
                TenantEntity(
                    propertyId = prop1Id,
                    propertyName = "Sunset Heights Apartments",
                    fullName = "Jane Wanjiru",
                    email = "jane.wanjiru@example.com",
                    phone = "+254 712 345 678",
                    unitNumber = "A-302",
                    rentAmount = 45000.0,
                    leaseStartDate = "2024-01-01",
                    leaseEndDate = "2025-12-31",
                    status = "Active"
                )
            ).toInt()

            val t2Id = tenantDao.insertTenant(
                TenantEntity(
                    propertyId = prop2Id,
                    propertyName = "Grandview Townhouses",
                    fullName = "David Ochieng",
                    email = "david.ochieng@example.com",
                    phone = "+254 722 987 654",
                    unitNumber = "TH-02",
                    rentAmount = 120000.0,
                    leaseStartDate = "2023-06-15",
                    leaseEndDate = "2025-06-14",
                    status = "Active"
                )
            ).toInt()

            val t3Id = tenantDao.insertTenant(
                TenantEntity(
                    propertyId = prop3Id,
                    propertyName = "Westlands Commercial Plaza",
                    fullName = "TechCorp Solutions Ltd",
                    email = "admin@techcorp.co.ke",
                    phone = "+254 733 112 233",
                    unitNumber = "Suite 4B",
                    rentAmount = 85000.0,
                    leaseStartDate = "2024-03-01",
                    leaseEndDate = "2026-02-28",
                    status = "Active"
                )
            ).toInt()

            // Seed Payments
            payDao.insertPayment(
                PaymentEntity(
                    tenantId = t1Id,
                    tenantName = "Jane Wanjiru",
                    propertyName = "Sunset Heights Apartments (A-302)",
                    amount = 45000.0,
                    paymentDate = "2024-11-02",
                    paymentMethod = "M-Pesa",
                    status = "Paid",
                    transactionRef = "MPX987654321"
                )
            )

            payDao.insertPayment(
                PaymentEntity(
                    tenantId = t2Id,
                    tenantName = "David Ochieng",
                    propertyName = "Grandview Townhouses (TH-02)",
                    amount = 120000.0,
                    paymentDate = "2024-11-05",
                    paymentMethod = "Bank Transfer",
                    status = "Paid",
                    transactionRef = "EFT-8839201"
                )
            )

            payDao.insertPayment(
                PaymentEntity(
                    tenantId = t3Id,
                    tenantName = "TechCorp Solutions Ltd",
                    propertyName = "Westlands Commercial Plaza (Suite 4B)",
                    amount = 85000.0,
                    paymentDate = "2024-11-10",
                    paymentMethod = "Bank Transfer",
                    status = "Pending",
                    transactionRef = "REF-PENDING-001"
                )
            )

            // Seed Maintenance Requests
            maintDao.insertRequest(
                MaintenanceRequestEntity(
                    propertyId = prop1Id,
                    propertyName = "Sunset Heights Apartments",
                    tenantName = "Jane Wanjiru (A-302)",
                    title = "Kitchen Sink Pipe Leaking",
                    category = "Plumbing",
                    priority = "High",
                    status = "Pending",
                    dateReported = "2024-11-12",
                    description = "Water leaking under the kitchen sink cabinet. Requires plumber inspection."
                )
            )

            maintDao.insertRequest(
                MaintenanceRequestEntity(
                    propertyId = prop2Id,
                    propertyName = "Grandview Townhouses",
                    tenantName = "David Ochieng (TH-02)",
                    title = "Main Gate Remote Sensor Repair",
                    category = "Electrical",
                    priority = "Medium",
                    status = "In Progress",
                    dateReported = "2024-11-08",
                    description = "Automatic gate remote control unresponsive intermittently."
                )
            )

            maintDao.insertRequest(
                MaintenanceRequestEntity(
                    propertyId = prop3Id,
                    propertyName = "Westlands Commercial Plaza",
                    tenantName = "TechCorp Solutions Ltd",
                    title = "AC Unit Servicing",
                    category = "HVAC",
                    priority = "Low",
                    status = "Resolved",
                    dateReported = "2024-10-25",
                    description = "Routine maintenance and filter replacement for Suite 4B air conditioner."
                )
            )
        }
    }
}
