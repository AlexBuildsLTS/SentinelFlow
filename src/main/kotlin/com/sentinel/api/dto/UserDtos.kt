package com.sentinel.api.dto

/**
 * Response DTO for User Profile.
 */
data class UserProfileResponse(
    val id: String,
    val email: String,
    val fullName: String,
    val username: String,
    val role: String,
    val status: String,
    val joinedAt: String
)

/**
 * Request DTO for updating the profile.
 */
data class UpdateProfileRequest(
    val fullName: String
)