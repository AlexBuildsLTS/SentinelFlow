package com.sentinel.repository.entity

import com.sentinel.domain.model.UserRole
import com.sentinel.domain.model.UserStatus
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.time.Instant.now as now

@Table("users")
data class User(
    @Id val id: String,
    val email: String,
    val username: String,
    val fullName: String? = null,
    val role: UserRole = UserRole.USER,     // ✅ Using Enum type
    val status: UserStatus = UserStatus.ACTIVE, // ✅ Using Enum type
    )


annotation class UserEntity
