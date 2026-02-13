package com.sentinel.service

import com.sentinel.domain.model.*
import com.sentinel.api.dto.SpendingTrend
import com.sentinel.repository.TransactionRepository
import com.sentinel.repository.entity.TransactionEntity
import com.sentinel.domain.usecase.TransactionResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service
import java.time.Instant

@Suppress("NewApi", "unused") // 🛡️ Kills the API 26 phantom error and unused warnings
@Service
class TransactionService(private val repository: TransactionRepository) {

    private val _transactionStream = MutableSharedFlow<Transaction>()
    val transactionStream = _transactionStream.asSharedFlow()

    suspend fun processTransaction(transaction: Transaction): TransactionResult {
        return try {
            // Mapping Domain -> Entity
            val entity = TransactionEntity(
                id = transaction.id.value,
                userId = transaction.userId.value,
                amount = transaction.amount.value,
                currency = transaction.currency,
                description = transaction.description,
                category = transaction.category,
                timestamp = transaction.timestamp,
                // Using a safe conversion to satisfy the linter
                processedAt = Instant.ofEpochMilli(System.currentTimeMillis())
            )

            repository.save(entity)
            _transactionStream.emit(transaction)
            TransactionResult.Success(transaction)
        } catch (e: Exception) {
            TransactionResult.InternalError("Database failure: ${e.message}")
        }
    }

    suspend fun getTotalVolume(userId: String): Double =
        repository.findTotalVolumeByUserId(userId) ?: 0.0

    suspend fun getTransactionCount(userId: String): Long =
        repository.countByUserId(userId)

    suspend fun getTrends(userId: String): List<SpendingTrend> =
        repository.findTrendsByUserId(userId)

    fun searchTransactions(start: Instant?, end: Instant?, category: String?, userId: String?): Flow<Transaction> {
        return repository.search(start, end, category, userId).map { entity ->
            Transaction(
                id = TransactionId(entity.id),
                amount = Amount(entity.amount),
                currency = entity.currency,
                description = entity.description,
                category = entity.category,
                userId = UserId(entity.userId),
                timestamp = entity.timestamp,
                processedAt = entity.processedAt
            )
        }
    }

    suspend fun findById(id: String): Transaction? {
        val entity = repository.findById(id) ?: return null
        return Transaction(
            id = TransactionId(entity.id),
            amount = Amount(entity.amount),
            currency = entity.currency,
            description = entity.description,
            category = entity.category,
            userId = UserId(entity.userId),
            timestamp = entity.timestamp,
            processedAt = entity.processedAt
        )
    }
}