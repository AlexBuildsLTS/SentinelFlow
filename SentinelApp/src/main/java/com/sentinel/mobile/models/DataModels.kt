package com.sentinel.mobile.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ==================================================================
// INTERNAL APP MODELS
// These are the clean data classes your UI and ViewModels will use.
// ==================================================================

@Serializable
enum class UserRole {
    @SerialName("ADMIN") ADMIN,
    @SerialName("MODERATOR") MODERATOR,
    @SerialName("PREMIUM") PREMIUM,
    @SerialName("MEMBER") MEMBER,
    @SerialName("USER") USER, // Adding USER as a fallback
    @SerialName("GUEST") GUEST
}

@Serializable
data class Transaction(
    val id: String,
    @SerialName("user_id") val userId: String,
    val amount: Double,
    @SerialName("merchant_name") val merchantName: String? = "Sentinel Store",
    @SerialName("risk_score") val riskScore: Double? = 0.0,
    @SerialName("transaction_date") val transactionDate: String
)

@Serializable
data class SupportTicket(
    val id: String,
    val user_id: String,
    val subject: String,
    val description: String,
    val status: String = "OPEN",
    val priority: String = "MEDIUM",
    @SerialName("created_at") val createdAt: String
)

// ✅ CRITICAL FIX: The internal ChatMessage model is now correct.
// 'senderRole' now uses the correct UserRole enum and 'timestamp' is removed.
data class ChatMessage(
    val id: String,
    val senderId: String,
    val receiverId: String,
    val content: String,
    val createdAt: String, // The single source of truth for time
    val isFromMe: Boolean,
    val senderName: String,
    val senderRole: UserRole // ✅ FIXED: Now uses your local UserRole enum
)
