package com.sentinel.domain.model

import java.time.Instant

/**
 * Core Domain Entity.
 * Immutable data structure representing the business state.
 */
data class Transaction(
    val id: TransactionId = TransactionId(),
    val amount: Amount,
    val currency: String,
    val description: String?,
    val category: String?,
    val userId: UserId,
    val timestamp: Instant,
    val processedAt: Instant? = null
)
