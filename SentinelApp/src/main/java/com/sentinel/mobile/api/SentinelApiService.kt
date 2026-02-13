package com.sentinel.mobile.api

import retrofit2.Response
import retrofit2.http.*
// ✅ Explicit imports ensuring these are found
import com.sentinel.mobile.api.LoginRequest
import com.sentinel.mobile.api.SupabaseAuthResponse
import com.sentinel.mobile.api.TransactionResponse

interface SentinelApiService {
    @Headers(
        "apikey: ${SupabaseConfig.KEY}",
        "Content-Type: application/json"
    )
    @POST("/auth/v1/token?grant_type=password")
    @Suppress("unused") // ✅ Silences "login is never used"
    suspend fun login(@Body request: LoginRequest): Response<SupabaseAuthResponse>

    // Fetch Real Transactions from your 'transactions' table
    @Headers(
        "apikey: ${SupabaseConfig.KEY}",
        "Content-Type: application/json"
    )
    @GET("/rest/v1/transactions?select=*")
    suspend fun getTransactions(@Header("Authorization") token: String): Response<List<TransactionResponse>>
}