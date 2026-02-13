package com.sentinel.api

import com.sentinel.api.dto.UpdateProfileRequest
import com.sentinel.api.dto.UserProfileResponse
import com.sentinel.service.UserService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.*

@Component
class UserHandler(private val userService: UserService) {

    suspend fun getMe(request: ServerRequest): ServerResponse {
        val principal = request.awaitPrincipal() as? UsernamePasswordAuthenticationToken
            ?: return status(401).buildAndAwait()

        val userId = principal.name
        val email = (principal.details as? Map<*, *>)?.get("email") as? String ?: "unknown@sentinel.com"

        val user = userService.ensureUserExists(userId, email)

        return ok().bodyValueAndAwait(UserProfileResponse(
            id = user.id,
            email = user.email,
            fullName = user.fullName ?: "", // Elvis only used if fullName is nullable
            username = user.username,
            role = user.role.toString(),   // ✅ Use .toString() or .name if it's an Enum
            status = user.status.toString(), // ✅ Use .toString() or .name if it's an Enum
            joinedAt = user.createdAt.toString()
        ))
    }

    suspend fun updateMe(request: ServerRequest): ServerResponse {
        val principal = request.awaitPrincipal() as? UsernamePasswordAuthenticationToken
        val userId = principal?.name ?: return status(401).buildAndAwait()

        val body = request.awaitBody<UpdateProfileRequest>()
        val updated = userService.updateProfile(userId, body)

        return ok().bodyValueAndAwait(UserProfileResponse(
            id = updated.id,
            email = updated.email,
            fullName = updated.fullName ?: "",
            username = updated.username,
            role = updated.role.toString(),
            status = updated.status.toString(),
            joinedAt = updated.createdAt.toString()
        ))
    }
}