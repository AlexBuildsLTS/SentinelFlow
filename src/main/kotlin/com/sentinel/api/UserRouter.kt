package com.sentinel.api

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class UserRouter {
    @Bean
    fun userRoutes(handler: UserHandler) = coRouter {
        "/api/users".nest {
            accept(MediaType.APPLICATION_JSON).nest {
                GET("/me", handler::getMe)
                PUT("/me", handler::updateMe)
            }
        }
    }
}