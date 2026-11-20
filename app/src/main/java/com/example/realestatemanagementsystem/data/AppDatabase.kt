package com.example.realestatemanagementsystem.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Property::class, Tenant::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun propertyDao(): PropertyDao
    abstract fun tenantDao(): TenantDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // One shared database for the whole app
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "real_estate_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
