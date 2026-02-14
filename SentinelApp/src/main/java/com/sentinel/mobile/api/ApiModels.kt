@file:OptIn(kotlinx.serialization.InternalSerializationApi::class, kotlinx.serialization.ExperimentalSerializationApi::class)

package com.sentinel.mobile.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val email: String, val pass: String)

@Serializable
data class TransactionResponse(
    val id: String,
    @SerialName("user_id") val userId: String,
    val amount: Double,
    val currency: String,
    val status: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("merchant_name") val merchantName: String,
    @SerialName("risk_score") val riskScore: Double = 0.0
)

@Serializable
data class SupabaseAuthResponse(
    @SerialName("access_token") val accessToken: String,
    val user: SupabaseUser
)

@Serializable
data class SupabaseUser(val id: String, val email: String)