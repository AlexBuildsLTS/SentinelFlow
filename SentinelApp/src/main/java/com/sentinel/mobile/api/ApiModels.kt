@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.sentinel.mobile.api

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.sentinel.mobile.models.UserRole

// --- REQUESTS ---
@Serializable
data class LoginRequest(
    val email: String,
    val pass: String
)

@Serializable
@Suppress("unused") // ✅ Silences "RegisterRequest is never used"
data class RegisterRequest(
    val email: String,
    val fullName: String,
    val pass: String
)

// --- RESPONSES ---

// Used for local mock (Old)
@Serializable
@Suppress("unused")
data class LoginResponse(
    val token: String,
    val user: UserProfileResponse
)

// ✅ REQUIRED FOR SUPABASE (New)
@Serializable
data class SupabaseAuthResponse(
    @SerialName("access_token") val accessToken: String,
    val user: SupabaseUser
)

@Serializable
data class SupabaseUser(
    val id: String,
    val email: String,
    @SerialName("user_metadata") val userMetadata: UserMetadata?
)

@Serializable
data class UserMetadata(
    @SerialName("full_name") val fullName: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    val role: String? = "USER"
)

@Serializable
data class UserProfileResponse(
    val id: String,
    val email: String,
    @SerialName("full_name") val fullName: String,
    val username: String,
    val role: UserRole,
    val status: String,
    @SerialName("joined_at") val joinedAt: String
)

@Serializable
data class TransactionResponse(
    val id: String,
    val amount: Double,
    val currency: String,
    val status: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("merchant_name") val merchantName: String,
    @SerialName("risk_score") val riskScore: Double = 0.0
)