package com.example.mobileappcat2.data.remote

import com.example.mobileappcat2.data.local.entity.TenantEntity
import com.google.gson.annotations.SerializedName

data class RemoteTenantDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("rent") val rent: String
) {
    fun toTenantEntity(): TenantEntity {
        return TenantEntity(
            id = (id?.toIntOrNull() ?: 0) + 1000,
            propertyId = 1,
            propertyName = "RentPulse Heights",
            fullName = name,
            email = "",
            phone = phone,
            unitNumber = "R-${id ?: "1"}",
            rentAmount = rent.toDoubleOrNull() ?: 0.0,
            leaseStartDate = "2026-01-01",
            leaseEndDate = "2026-12-31",
            status = "Active"
        )
    }

    companion object {
        fun fromTenantEntity(entity: TenantEntity): RemoteTenantDto {
            return RemoteTenantDto(
                id = (entity.id - 1000).toString(),
                name = entity.fullName,
                phone = entity.phone,
                rent = entity.rentAmount.toString()
            )
        }
    }
}
