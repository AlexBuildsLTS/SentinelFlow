package com.sentinel.api

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class TransactionRouter {

    @Bean
    fun transactionRoutes(handler: TransactionHandler) = coRouter {
        "/api".nest {
            accept(MediaType.APPLICATION_JSON).nest {
                // ✅ FIXED: Using explicit lambdas to solve the routing type errors
                GET("/dashboard/stats") { handler.getDashboardStats(it) }
                GET("/transactions") { handler.searchTransactions(it) }
                GET("/transactions/{id}") { handler.getTransaction(it) }
                POST("/transactions") { handler.createTransaction(it) }
                GET("/transactions/trends") { handler.getTrends(it) }
            }

            accept(MediaType.TEXT_EVENT_STREAM).nest {
                GET("/transactions/stream") { handler.streamTransactions(it) }
            }
        }
    }
}