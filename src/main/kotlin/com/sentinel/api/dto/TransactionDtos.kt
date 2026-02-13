package com.sentinel.api.dto

import com.sentinel.domain.model.Amount
import com.sentinel.domain.model.Transaction
import com.sentinel.domain.model.UserId
import java.math.BigDecimal
import java.time.Instant

/**
 * Request body for creating a transaction.
 */
data class CreateTransactionRequest(
    val amount: BigDecimal,
    val currency: String,
    val description: String?,
    val category: String?,
    val userId: String,
    val timestamp: Instant
) {
    fun toDomain() = Transaction(
        amount = Amount(amount),
        currency = currency,
        description = description,
        category = category,
        userId = UserId(userId),
        timestamp = timestamp
    )
}

/**
 * Response body for a successful transaction.
 */
data class TransactionResponse(
    val id: String,
    val status: String,
    val processedAt: String
)

/**
 * DTO for aggregated spending data.
 */
data class SpendingTrend(
    val date: String,
    val total: BigDecimal
)