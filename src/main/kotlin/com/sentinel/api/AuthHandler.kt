package com.sentinel.api

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.ServerResponse.status

@Component
class AuthHandler {

    /**
     * Returns the current authenticated user's ID extracted from the Supabase Token.
     */
    suspend fun getCurrentUser(request: ServerRequest): ServerResponse {
        val principal = request.awaitPrincipal() 
            ?: return status(401).bodyValueAndAwait(mapOf("error" to "Unauthorized"))
            
        return ok().bodyValueAndAwait(mapOf(
            "id" to principal.name,
            "provider" to "supabase"
        ))
    }
}