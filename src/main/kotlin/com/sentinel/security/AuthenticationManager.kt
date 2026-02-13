package com.sentinel.security

import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AuthenticationManager(
    private val jwtUtil: JwtUtil
) : ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        val authToken = authentication.credentials.toString()

        return Mono.fromCallable {
            try {
                // ✅ FIXED: Using the correct method name from JwtUtil
                val claims = jwtUtil.getClaimsFromToken(authToken)

                // ✅ FIXED: subject is a property of Claims in JJWT
                val userId = (claims["sub"] as? String) ?: ""
                val role = claims["role"] as? String ?: ""
                val email = claims["email"] as? String ?: "unknown@sentinel.com"

                // Supabase "authenticated" role -> Spring ROLE_USER
                val authorities = if (role.equals("authenticated", ignoreCase = true)) {
                    listOf(SimpleGrantedAuthority("ROLE_USER"))
                } else {
                    listOf(SimpleGrantedAuthority("ROLE_GUEST"))
                }

                val auth = UsernamePasswordAuthenticationToken(
                    userId,
                    authToken,
                    authorities
                )
                auth.details = mapOf("email" to email)
                auth
            } catch (e: Exception) {
                // Return null to signify authentication failure
                null
            }
        }.flatMap { result ->
            if (result != null) Mono.just(result) else Mono.empty()
        }
    }
}