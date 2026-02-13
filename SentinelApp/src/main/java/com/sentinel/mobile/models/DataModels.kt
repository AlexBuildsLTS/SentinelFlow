@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.sentinel.mobile.models

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
enum class UserRole {
    ADMIN, USER, GUEST, MODERATOR
}

@Serializable
@Suppress("unused") // ✅ Silences "INCOME/TRANSFER is never used"
enum class TransactionType {
    INCOME, EXPENSE, TRANSFER
}

@Serializable
@Suppress("unused") // ✅ Silences "UserProfile is never used"
data class UserProfile(
    val id: String,
    val email: String,
    val fullName: String,
    val username: String,
    val role: UserRole,
    val status: String,
    val joinedAt: String
)

@Serializable
data class Transaction(
    val id: String,
    val amount: Double,
    val currency: String,
    val status: String,
    val timestamp: String,
    val merchantName: String,
    val type: TransactionType = TransactionType.EXPENSE,
    val category: String = "General"
)