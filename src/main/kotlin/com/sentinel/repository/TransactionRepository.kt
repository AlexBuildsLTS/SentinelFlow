package com.sentinel.repository

import com.sentinel.api.dto.SpendingTrend
import com.sentinel.repository.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface TransactionRepository : CoroutineCrudRepository<TransactionEntity, String> {

    @Query("SELECT SUM(amount) FROM transactions WHERE user_id = :userId")
    suspend fun findTotalVolumeByUserId(userId: String): Double?

    @Query("SELECT COUNT(*) FROM transactions WHERE user_id = :userId")
    suspend fun countByUserId(userId: String): Long

    @Query("""
        SELECT CAST(timestamp AS DATE) as date, SUM(amount) as total 
        FROM transactions 
        WHERE user_id = :userId 
        GROUP BY CAST(timestamp AS DATE) 
        ORDER BY date ASC 
        LIMIT 30
    """)
    suspend fun findTrendsByUserId(userId: String): List<SpendingTrend>

    @Query("""
        SELECT * FROM transactions 
        WHERE (:userId IS NULL OR user_id = :userId)
        AND (:category IS NULL OR category = :category)
        AND (:start IS NULL OR timestamp >= :start)
        AND (:end IS NULL OR timestamp <= :end)
        ORDER BY timestamp DESC
    """)
    fun search(start: Instant?, end: Instant?, category: String?, userId: String?): Flow<TransactionEntity>
}