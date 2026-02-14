@file:OptIn(kotlinx.serialization.ExperimentalSerializationApi::class, kotlinx.serialization.InternalSerializationApi::class)
@file:Suppress("unused")

package com.sentinel.mobile.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Global User Roles for the SentinelFlow System.
 */
@Serializable
enum class UserRole {
    ADMIN,
    MODERATOR,
    PREMIUM,
    MEMBER,
    USER,
    GUEST
}

/**
 * Status Enums for Support Ticket Management.
 */
@Serializable
enum class TicketStatus {
    OPEN,
    IN_PROGRESS,
    RESOLVED,
    CLOSED
}

/**
 * Transaction Categories for Financial Monitoring.
 */
@Serializable
enum class TransactionType {
    INCOME,
    EXPENSE,
    TRANSFER
}

/**
 * Core Data Class for Private and Neural Messaging.
 */
@Serializable
data class ChatMessage(
    val id: String,
    val content: String,
    val timestamp: String,
    val isFromMe: Boolean,
    val senderName: String,
    val senderAvatar: String? = null,
    val senderRole: UserRole = UserRole.USER
)

/**
 * Data Model for System Notifications and Audit Alerts.
 */
@Serializable
data class Notification(
    val id: String,
    val message: String,
    val timestamp: String
)

/**
 * Administrative Model for Support Ticket Objects.
 */
@Serializable
data class SupportTicket(
    val id: String,
    val subject: String,
    val description: String,
    val status: TicketStatus,
    val priority: String,
    @SerialName("created_at") val createdAt: String
)

/**
 * Financial Transaction Model for Executive Dashboard.
 */
@Serializable
data class Transaction(
    val id: String,
    @SerialName("user_id") val userId: String,
    val amount: Double,
    val currency: String = "USD",
    val status: String,
    @SerialName("transaction_date") val transactionDate: String,
    @SerialName("merchant_name") val merchantName: String?,
    val type: TransactionType = TransactionType.EXPENSE,
    val category: String = "General",
    @SerialName("risk_score") val riskScore: Double? = 0.0
)