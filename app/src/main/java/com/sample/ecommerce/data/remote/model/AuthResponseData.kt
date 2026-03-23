package com.sample.ecommerce.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Auth success response data - nested inside ApiResponse.data for login/signUp.
 */
data class AuthResponseData(
    @SerializedName("user_id") val userId: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("token") val token: String
)
