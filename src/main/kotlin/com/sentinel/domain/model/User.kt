package com.sentinel.domain.model

import java.time.Instant

/**
 * FIXED USER MODEL
 * Removed manual getUsername() to prevent JVM signature clash with property getter.
 */
data class User(
    val id: String,
    val email: String,
    val fullName: String?,
    val username: String, // Kotlin automatically generates getUsername() for this
    val role: UserRole,
    val status: UserStatus,
    val createdAt: Instant
)

enum class UserRole {
    ADMIN, MODERATOR, USER, GUEST
}

enum class UserStatus {
    ACTIVE, INACTIVE, BANNED
}