package com.sentinel.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer

/**
 * CORS Configuration.
 * Essential for "Full Stack" development where frontend and backend run on different ports.
 * Configures the Reactive WebFlux security layer to accept requests from all origins (for dev).
 */
@Configuration
class WebConfig : WebFluxConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("*") // In production, replace with specific frontend URL
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
    }
}
