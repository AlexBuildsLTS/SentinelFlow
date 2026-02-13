package com.sentinel.domain.model

import java.math.BigDecimal
import java.util.UUID

/**
 * Value Class for Transaction IDs.
 * Prevents swapping ID strings (e.g., mixing UserId and TransactionId).
 * Compiles to a String at runtime for zero overhead.
 */
@JvmInline
value class TransactionId(val value: String = UUID.randomUUID().toString())

/**
 * Value Class for User IDs.
 */
@JvmInline
value class UserId(val value: String)

/**
 * Domain representation of Money.
 * Enforces business rules (non-negative) upon instantiation.
 */
@JvmInline
value class Amount(val value: BigDecimal) {
    init {
        require(value >= BigDecimal.ZERO) { "Amount cannot be negative" }
    }
}
