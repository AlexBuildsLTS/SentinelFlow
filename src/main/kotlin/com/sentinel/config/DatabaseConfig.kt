package com.sentinel.config

import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator

/**
 * Database Initializer.
 * R2DBC does not support auto-DDL (Hibernate hbm2ddl) out of the box.
 * This config runs schema.sql when the application starts.
 */
@Configuration
class DatabaseConfig {

    @Bean
    fun initializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer {
        val initializer = ConnectionFactoryInitializer()
        initializer.setConnectionFactory(connectionFactory)
        initializer.setDatabasePopulator(
            ResourceDatabasePopulator(ClassPathResource("schema.sql"))
        )
        return initializer
    }
}
