package com.sample.ecommerce.data.local

import com.sample.ecommerce.data.local.entity.UserEntity
import com.sample.ecommerce.data.remote.model.AuthResponseData
import com.sample.ecommerce.domain.model.User

/**
 * Provides auth from local DB and returns data in the same shape as the API ([AuthResponseData])
 * so the app can use local storage when the API is unavailable.
 */
class AuthLocalDataSource(
    private val database: com.sample.ecommerce.data.local.AppDatabase
) {
    private val userDao = database.userDao()

    companion object {
        private const val LOCAL_TOKEN_PREFIX = "local_"
    }

    suspend fun login(email: String, password: String): Result<AuthResponseData> = runCatching {
        val entity = userDao.findByEmailAndPassword(email, password)
            ?: throw LocalAuthException("Invalid email or password")
        entity.toAuthResponseData()
    }.recoverCatching { e ->
        if (e is LocalAuthException) throw e
        throw LocalAuthException(e.message ?: "Login failed")
    }

    suspend fun signUp(user: User): Result<AuthResponseData> = runCatching {
        val existing = userDao.findByEmail(user.email)
        if (existing != null) throw LocalAuthException("Email already registered")
        val entity = UserEntity(
            id = 0L,
            email = user.email,
            password = user.password,
            fullName = user.fullName,
            phone = user.phone,
            address = user.address
        )
        val id = userDao.insert(entity)
        AuthResponseData(
            userId = id.toString(),
            name = entity.fullName,
            email = entity.email,
            token = "$LOCAL_TOKEN_PREFIX$id"
        )
    }.recoverCatching { e ->
        if (e is LocalAuthException) throw e
        throw LocalAuthException(e.message ?: "Sign up failed")
    }

    private fun UserEntity.toAuthResponseData() = AuthResponseData(
        userId = id.toString(),
        name = fullName,
        email = email,
        token = "$LOCAL_TOKEN_PREFIX$id"
    )
}

class LocalAuthException(override val message: String) : Exception(message)
