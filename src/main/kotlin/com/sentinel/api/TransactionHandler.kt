package com.sentinel.api

import com.sentinel.api.dto.CreateTransactionRequest
import com.sentinel.api.dto.TransactionResponse
import com.sentinel.domain.usecase.TransactionResult
import com.sentinel.service.TransactionService
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.awaitPrincipal
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait
import java.time.Instant
import java.time.Instant.now

@Component
class TransactionHandler(private val service: TransactionService) {

    suspend fun createTransaction(request: ServerRequest): ServerResponse {
        val principal = request.awaitPrincipal() as? UsernamePasswordAuthenticationToken
        val userId = principal?.name ?: return ServerResponse.status(401).buildAndAwait()

        val requestDto = request.awaitBody<CreateTransactionRequest>()
        val domainTransaction = requestDto.copy(userId = userId).toDomain()

        return when (val result = service.processTransaction(domainTransaction)) {
            is TransactionResult.Success -> ServerResponse.status(201).bodyValueAndAwait(
                TransactionResponse(result.transaction.id.value, "COMPLETED", result.transaction.processedAt.toString())
            )
            is TransactionResult.ValidationError -> ServerResponse.badRequest().bodyValueAndAwait(mapOf("error" to result.message))
            is TransactionResult.InternalError -> ServerResponse.status(500).bodyValueAndAwait(mapOf("error" to result.message))
        }
    }

    suspend fun getDashboardStats(request: ServerRequest): ServerResponse {
        val principal = request.awaitPrincipal() as? UsernamePasswordAuthenticationToken
        val userId = principal?.name ?: return ServerResponse.status(401).buildAndAwait()

        val stats = mapOf<String, Any>(
            "totalVolume" to service.getTotalVolume(userId),
            "transactionCount" to service.getTransactionCount(userId)
        )
        return ServerResponse.ok().bodyValueAndAwait(stats)
    }

    suspend fun getTrends(request: ServerRequest): ServerResponse {
        val principal = request.awaitPrincipal() as? UsernamePasswordAuthenticationToken
        val userId = principal?.name ?: return ServerResponse.status(401).buildAndAwait()

        val trends = service.getTrends(userId)
        return ServerResponse.ok().bodyValueAndAwait(trends)
    }

    suspend fun searchTransactions(request: ServerRequest): ServerResponse {
        val principal = request.awaitPrincipal() as? UsernamePasswordAuthenticationToken
        val userId = principal?.name ?: return ServerResponse.status(401).buildAndAwait()

        val transactions = service.searchTransactions(null, null, null, userId).toList()
        return ServerResponse.ok().bodyValueAndAwait(transactions)
    }

    // ✅ FIXED: Added missing getTransaction method
    suspend fun getTransaction(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id")
        val transaction = service.findById(id) ?: return ServerResponse.status(404).buildAndAwait()
        return ServerResponse.ok().bodyValueAndAwait(transaction)
    }

    suspend fun streamTransactions(request: ServerRequest): ServerResponse {
        val flow = service.transactionStream.map { tx ->
            mapOf("amount" to tx.amount.value, "category" to tx.category, "timestamp" to tx.timestamp.toString())
        }
        return ServerResponse.ok()
            .contentType(MediaType.TEXT_EVENT_STREAM)
            .bodyAndAwait(flow)
    }
}