package com.sample.ecommerce.domain.repository

import com.sample.ecommerce.domain.model.User

interface UserRepository {
    suspend fun signUp(user: User): Result<Long>
    suspend fun login(email: String, password: String): Result<User?>
    suspend fun getUserById(userId: Long): User?
    suspend fun getCurrentUserId(): Long?
    suspend fun setCurrentUserId(userId: Long?)
    suspend fun logout()
}
