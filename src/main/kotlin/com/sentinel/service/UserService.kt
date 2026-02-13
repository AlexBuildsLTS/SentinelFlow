package com.sentinel.service

import com.sentinel.repository.UserRepository
import com.sentinel.repository.entity.UserEntity
import com.sentinel.api.dto.UpdateProfileRequest
import com.sentinel.domain.model.User
import com.sentinel.domain.model.UserRole
import com.sentinel.domain.model.UserStatus
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class UserService(private val repository: UserRepository) {

    /**
     * Retrieves a user by their ID, or creates a new one if they don't exist.
     *
     * If the user is not found in the repository, a new `UserEntity` is created
     * with default values:
     * - The username is derived from the part of the email before the "@" symbol.
     * - The role is set to `UserRole.USER`.
     * - The status is set to `UserStatus.ACTIVE`.
     *
     * @param userId The unique identifier of the user.
     * @param email The user's email address.
     * @return The existing or newly created `UserEntity`.
     */
    suspend fun ensureUserExists(userId: String, email: String): User {
        return repository.findById(userId) ?: repository.save(
            UserEntity(
                id = userId,
                email = email,
                username = email.substringBefore("@"),
                role = UserRole.USER,     // ✅ Passing Enum, not String
                status = UserStatus.ACTIVE // ✅ Passing Enum, not String
            )
        )
    }

    annotation class UserEntity(
        val id: String,
        val email: String,
        val username: String,
        val role: com.sentinel.domain.model.UserRole,
        val status: com.sentinel.domain.model.UserStatus
    )

    suspend fun updateProfile(userId: String, request: UpdateProfileRequest): User {
        val user = repository.findById(userId) ?: throw Exception("User not found")
        val updated = user.copy(fullName = request.fullName)
        return repository.save(updated)
    }
}