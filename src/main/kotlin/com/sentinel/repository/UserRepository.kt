package com.sentinel.repository

import com.sentinel.domain.model.User
import com.sentinel.repository.entity.UserEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CoroutineCrudRepository<User, String> {
    suspend fun findByUsername(username: String): User?
    suspend fun findByEmail(email: String): User?
    fun save(entity: UserEntity): User
}