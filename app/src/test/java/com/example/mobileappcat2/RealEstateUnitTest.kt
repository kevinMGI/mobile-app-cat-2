package com.example.mobileappcat2

import com.example.mobileappcat2.data.local.entity.MaintenanceRequestEntity
import com.example.mobileappcat2.data.local.entity.PaymentEntity
import com.example.mobileappcat2.data.local.entity.PropertyEntity
import com.example.mobileappcat2.data.local.entity.TenantEntity
import org.junit.Assert.*
import org.junit.Test

class RealEstateUnitTest {

    @Test
    fun propertyEntity_creationAndValidation() {
        val property = PropertyEntity(
            id = 1,
            title = "Sunset Heights",
            address = "Parklands Ave",
            propertyType = "Apartment",
            unitsCount = 12,
            monthlyRent = 45000.0,
            status = "Occupied",
            description = "Spacious apartments"
        )

        assertEquals("Sunset Heights", property.title)
        assertEquals(12, property.unitsCount)
        assertEquals(45000.0, property.monthlyRent, 0.01)
        assertTrue(property.monthlyRent > 0)
    }

    @Test
    fun tenantEntity_creationAndValidation() {
        val tenant = TenantEntity(
            id = 1,
            propertyId = 10,
            propertyName = "Sunset Heights",
            fullName = "Jane Wanjiru",
            email = "jane@example.com",
            phone = "+254712345678",
            unitNumber = "A-302",
            rentAmount = 45000.0,
            leaseStartDate = "2024-01-01",
            leaseEndDate = "2025-12-31",
            status = "Active"
        )

        assertEquals("Jane Wanjiru", tenant.fullName)
        assertEquals("A-302", tenant.unitNumber)
        assertEquals("Active", tenant.status)
        assertTrue(tenant.email.contains("@"))
    }

    @Test
    fun paymentEntity_revenueCalculation() {
        val payments = listOf(
            PaymentEntity(1, 1, "Jane", "Sunset", 45000.0, "2024-11-01", "M-Pesa", "Paid", "REF1"),
            PaymentEntity(2, 2, "David", "Grandview", 120000.0, "2024-11-05", "Bank Transfer", "Paid", "REF2"),
            PaymentEntity(3, 3, "TechCorp", "Plaza", 85000.0, "2024-11-10", "Cash", "Pending", "REF3")
        )

        val totalPaid = payments.filter { it.status == "Paid" }.sumOf { it.amount }
        val totalPending = payments.filter { it.status == "Pending" }.sumOf { it.amount }

        assertEquals(165000.0, totalPaid, 0.01)
        assertEquals(85000.0, totalPending, 0.01)
    }

    @Test
    fun maintenanceRequest_filtering() {
        val requests = listOf(
            MaintenanceRequestEntity(1, 1, "Sunset", "Jane", "Sink Leak", "Plumbing", "High", "Pending", "2024-11-12", "Desc"),
            MaintenanceRequestEntity(2, 2, "Grandview", "David", "Gate Sensor", "Electrical", "Medium", "In Progress", "2024-11-08", "Desc"),
            MaintenanceRequestEntity(3, 3, "Plaza", "TechCorp", "AC Servicing", "HVAC", "Low", "Resolved", "2024-10-25", "Desc")
        )

        val activeCount = requests.count { it.status != "Resolved" }
        val highPriorityCount = requests.count { it.priority == "High" }

        assertEquals(2, activeCount)
        assertEquals(1, highPriorityCount)
    }
}
