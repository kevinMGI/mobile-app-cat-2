package com.example.mobileappcat2.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RentPulseApiService {
    @GET("tenants")
    suspend fun getAllTenants(): Response<List<RemoteTenantDto>>

    @POST("tenants")
    suspend fun createTenant(@Body tenant: RemoteTenantDto): Response<RemoteTenantDto>

    @PUT("tenants/{id}")
    suspend fun updateTenant(@Path("id") id: String, @Body tenant: RemoteTenantDto): Response<RemoteTenantDto>

    @DELETE("tenants/{id}")
    suspend fun deleteTenant(@Path("id") id: String): Response<Void>
}
