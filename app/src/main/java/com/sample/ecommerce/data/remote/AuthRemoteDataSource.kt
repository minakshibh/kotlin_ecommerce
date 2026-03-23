package com.sample.ecommerce.data.remote

import com.google.gson.JsonParser
import com.sample.ecommerce.data.remote.model.AuthResponseData
import com.sample.ecommerce.data.remote.model.LoginRequest
import com.sample.ecommerce.data.remote.model.SignUpRequest
import retrofit2.HttpException
import java.io.IOException

/**
 * Remote data source for auth APIs. Wraps [AuthApiService] and maps responses to [Result].
 */
class AuthRemoteDataSource(
    private val authApiService: AuthApiService = RetrofitClient.authApiService
) {

    suspend fun login(email: String, password: String): Result<AuthResponseData> = runCatching {
        val response = authApiService.login(LoginRequest(email = email, password = password))
        if (response.success && response.data != null) {
            response.data
        } else {
            throw ApiException(response.code, response.message)
        }
    }.recoverCatching { e ->
        when (e) {
            is ApiException -> throw e
            is HttpException -> throw ApiException(
                code = e.code(),
                message = e.response()?.errorBody()?.string()?.let { parseMessage(it) }
                    ?: e.message()
            )
            is IOException -> throw ApiException(-1, "Network error: ${e.message}")
            else -> throw ApiException(-1, e.message ?: "Unknown error")
        }
    }

    suspend fun signUp(
        name: String,
        email: String,
        password: String,
        phone: String? = null,
        address: String? = null
    ): Result<AuthResponseData> = runCatching {
        val response = authApiService.signUp(
            SignUpRequest(
                name = name,
                email = email,
                password = password,
                phone = phone,
                address = address
            )
        )
        if (response.success && response.data != null) {
            response.data
        } else {
            throw ApiException(response.code, response.message)
        }
    }.recoverCatching { e ->
        when (e) {
            is ApiException -> throw e
            is HttpException -> throw ApiException(
                code = e.code(),
                message = e.response()?.errorBody()?.string()?.let { parseMessage(it) }
                    ?: e.message()
            )
            is IOException -> throw ApiException(-1, "Network error: ${e.message}")
            else -> throw ApiException(-1, e.message ?: "Unknown error")
        }
    }

    private fun parseMessage(errorBody: String): String {
        return try {
            val json = JsonParser.parseString(errorBody).asJsonObject
            if (json.has("message")) json.get("message").asString else errorBody
        } catch (_: Exception) {
            errorBody
        }
    }
}

/**
 * Thrown when API returns success=false or non-2xx.
 */
class ApiException(val code: Int, override val message: String) : Exception(message)
