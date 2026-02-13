package com.sentinel.domain.usecase

import com.sentinel.domain.model.Transaction

/**
 * FIXED SEALED CLASS
 * Changed InternalError from 'data object' to 'data class' to support error messages.
 */
sealed class TransactionResult {
    data class Success(val transaction: Transaction) : TransactionResult()
    data class ValidationError(val message: String) : TransactionResult()
    data class InternalError(val message: String) : TransactionResult()
}