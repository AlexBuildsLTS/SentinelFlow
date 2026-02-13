package com.sentinel.api

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class SentinelRouter {
    @Bean
    fun apiRoutes(
        txHandler: TransactionHandler,
        userHandler: UserHandler,
        authHandler: AuthHandler
    ) = coRouter {
        "/api".nest {
            // 🔒 SECURE JSON API
            accept(MediaType.APPLICATION_JSON).nest {
                // Identity & Profile
                GET("/auth/me", authHandler::getCurrentUser)
                GET("/users/me", userHandler::getMe)
                PUT("/users/me", userHandler::updateMe)

                // Financial Core
                GET("/dashboard/stats", txHandler::getDashboardStats)
                GET("/transactions/trends", txHandler::getTrends)
                GET("/transactions", txHandler::searchTransactions)
                GET("/transactions/{id}", txHandler::getTransaction)
                POST("/transactions", txHandler::createTransaction)
            }

            // ⚡ REAL-TIME SSE
            accept(MediaType.TEXT_EVENT_STREAM).nest {
                GET("/transactions/stream", txHandler::streamTransactions)
            }
        }
    }
}