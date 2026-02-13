package com.sentinel.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val username: String, val password: String)

@Serializable
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val fullName: String
)

@Serializable
data class AuthResponse(val token: String, val message: String)

@Serializable
data class UserResponse(
    val id: String,
    val username: String,
    val email: String,
    val fullName: String,
    val roles: String,
    val createdAt: String
)