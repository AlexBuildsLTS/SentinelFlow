package com.sentinel.mobile.api

import com.sentinel.mobile.models.Transaction
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.*

// ==================================================================
// DATA TRANSFER OBJECTS (DTOs)
// These now correctly match your Supabase schema and API responses.
// ==================================================================

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class SignUpRequest(
    val email: String,
    val password: String,
    val data: Map<String, String>? = null
)

@Serializable
data class ResetRequest(
    val email: String
)

@Serializable
data class SupabaseUser(
    val id: String,
    val email: String? = null
)

@Serializable
data class SupabaseAuthResponse(
    @SerialName("access_token") val accessToken: String,
    val user: SupabaseUser
)

// ✅ CRITICAL FIX: DTO for the User's profile information when joined from another table.
@Serializable
data class UserProfileDto(
    @SerialName("full_name") val fullName: String? = null,
    @SerialName("role") val role: String? = null // Supabase sends this as a string
)

// ✅ CRITICAL FIX: This DTO now correctly represents a chat message from Supabase,
// including the nested sender profile information.
@Serializable
data class ChatLogDto(
    @SerialName("id") val id: String,
    @SerialName("sender_id") val senderId: String,
    @SerialName("receiver_id") val receiverId: String,
    @SerialName("content") val content: String,
    @SerialName("created_at") val createdAt: String? = null,
    // This field holds the joined data from the 'users' table.
    // In your Supabase query, you must select: `*, sender_profile:sender_id(*)`
    @SerialName("sender_profile") val senderProfile: UserProfileDto? = null
)

@Serializable
data class SendMessageRequest(
    @SerialName("sender_id") val senderId: String,
    @SerialName("receiver_id") val receiverId: String,
    @SerialName("content") val content: String
)

// ==================================================================
// THE MASTER API INTERFACE
// ==================================================================

interface SentinelApiService {

    @POST("/auth/v1/token?grant_type=password")
    suspend fun login(
        @Body request: LoginRequest,
        @Header("apikey") apiKey: String = SupabaseConfig.API_KEY
    ): Response<SupabaseAuthResponse>

    @POST("/auth/v1/signup")
    suspend fun signUp(
        @Body request: SignUpRequest,
        @Header("apikey") apiKey: String = SupabaseConfig.API_KEY
    ): Response<SupabaseAuthResponse>

    @POST("/auth/v1/recover")
    suspend fun resetPassword(
        @Body request: ResetRequest,
        @Header("apikey") apiKey: String = SupabaseConfig.API_KEY
    ): Response<Void>

    @GET("/rest/v1/transactions?select=*&order=created_at.desc")
    suspend fun getTransactions(
        @Header("Authorization") token: String,
        @Header("apikey") apiKey: String = SupabaseConfig.API_KEY
    ): Response<List<Transaction>>

    // ✅ CRITICAL FIX: Added the missing @Query("select") parameter.
    @GET("/rest/v1/chat_messages?order=created_at.asc")
    suspend fun getChatHistory(
        @Header("Authorization") token: String,
        @Query("select") select: String, // e.g., "*,sender_profile:sender_id(full_name,role)"
        @Query("or") orFilter: String,
        @Header("apikey") apiKey: String = SupabaseConfig.API_KEY
    ): Response<List<ChatLogDto>>

    @POST("/rest/v1/chat_messages")
    suspend fun sendMessage(
        @Header("Authorization") token: String,
        @Body message: SendMessageRequest,
        @Header("apikey") apiKey: String = SupabaseConfig.API_KEY
    ): Response<Void>
}
