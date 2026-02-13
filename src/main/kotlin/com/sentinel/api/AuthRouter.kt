package com.sentinel.api

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class AuthRouter {
    @Bean
    fun authRoutes(handler: AuthHandler) = coRouter {
        "/api/auth".nest {
            accept(MediaType.APPLICATION_JSON).nest {
                GET("/me", handler::getCurrentUser)
            }
        }
    }
}