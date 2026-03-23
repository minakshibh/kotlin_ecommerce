package com.sample.ecommerce.data.remote

import com.sample.ecommerce.data.remote.model.ApiResponse
import com.sample.ecommerce.data.remote.model.AuthResponseData
import com.sample.ecommerce.data.remote.model.LoginRequest
import com.sample.ecommerce.data.remote.model.SignUpRequest
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Authentication API endpoints.
 * Base URL is configured in [RetrofitClient].
 */
interface AuthApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<AuthResponseData>

    @POST("auth/register")
    suspend fun signUp(@Body request: SignUpRequest): ApiResponse<AuthResponseData>
}
