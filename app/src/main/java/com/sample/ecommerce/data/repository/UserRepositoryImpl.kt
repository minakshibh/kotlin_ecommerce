package com.sample.ecommerce.data.repository

import com.sample.ecommerce.data.local.AppDatabase
import com.sample.ecommerce.data.local.AuthLocalDataSource
import com.sample.ecommerce.data.local.SessionManager
import com.sample.ecommerce.data.local.entity.UserEntity
import com.sample.ecommerce.data.remote.ApiException
import com.sample.ecommerce.data.remote.AuthRemoteDataSource
import com.sample.ecommerce.domain.model.User
import com.sample.ecommerce.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val database: AppDatabase,
    private val sessionManager: SessionManager,
    private val authRemoteDataSource: AuthRemoteDataSource = AuthRemoteDataSource(),
    private val authLocalDataSource: AuthLocalDataSource = AuthLocalDataSource(database)
) : UserRepository {

    private val userDao = database.userDao()

    override suspend fun signUp(user: User): Result<Long> = withContext(Dispatchers.IO) {
        val remoteResult = authRemoteDataSource.signUp(
            name = user.fullName,
            email = user.email,
            password = user.password,
            phone = user.phone,
            address = user.address
        )
        remoteResult.fold(
            onSuccess = { data ->
                val userId = data.userId.toLongOrNull() ?: 0L
                saveAuthUserAndSession(data, userId)
                Result.success(userId)
            },
            onFailure = { _ ->
                // API unavailable or failed – use local storage and same response shape
                authLocalDataSource.signUp(user).fold(
                    onSuccess = { data ->
                        val userId = data.userId.toLongOrNull() ?: 0L
                        saveAuthUserAndSession(data, userId)
                        Result.success(userId)
                    },
                    onFailure = { e ->
                        Result.failure(Exception(e.message))
                    }
                )
            }
        )
    }

    override suspend fun login(email: String, password: String): Result<User?> = withContext(Dispatchers.IO) {
        val remoteResult = authRemoteDataSource.login(email, password)
        remoteResult.fold(
            onSuccess = { data ->
                val userId = data.userId.toLongOrNull() ?: 0L
                saveAuthUserAndSession(data, userId)
                Result.success(
                    User(
                        id = userId,
                        email = data.email,
                        password = "",
                        fullName = data.name,
                        phone = null,
                        address = null
                    )
                )
            },
            onFailure = { _ ->
                // API unavailable or failed – use local storage and same response shape
                authLocalDataSource.login(email, password).fold(
                    onSuccess = { data ->
                        val userId = data.userId.toLongOrNull() ?: 0L
                        saveAuthUserAndSession(data, userId)
                        Result.success(
                            User(
                                id = userId,
                                email = data.email,
                                password = "",
                                fullName = data.name,
                                phone = null,
                                address = null
                            )
                        )
                    },
                    onFailure = { e ->
                        Result.failure(Exception(e.message))
                    }
                )
            }
        )
    }

    private suspend fun saveAuthUserAndSession(data: com.sample.ecommerce.data.remote.model.AuthResponseData, userId: Long) {
        userDao.insertOrReplace(
            UserEntity(
                id = userId,
                email = data.email,
                password = "",
                fullName = data.name,
                phone = null,
                address = null
            )
        )
        sessionManager.setCurrentUserId(userId)
        sessionManager.setAuthToken(data.token)
    }

    override suspend fun getUserById(userId: Long): User? = withContext(Dispatchers.IO) {
        userDao.getById(userId)?.toDomain()
    }

    override suspend fun getCurrentUserId(): Long? = sessionManager.getCurrentUserId()

    override suspend fun setCurrentUserId(userId: Long?) {
        sessionManager.setCurrentUserId(userId)
    }

    override suspend fun logout() {
        sessionManager.logout()
    }

    private fun UserEntity.toDomain() = User(
        id = id,
        email = email,
        password = password,
        fullName = fullName,
        phone = phone,
        address = address
    )
}
