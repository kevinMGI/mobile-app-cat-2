package com.example.mobileappcat2.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://6b000cfe7fc363ccff09bc0a.mockapi.io/"

    val apiService: RentPulseApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RentPulseApiService::class.java)
    }
}
