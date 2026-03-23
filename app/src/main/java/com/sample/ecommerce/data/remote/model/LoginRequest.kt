package com.sample.ecommerce.data.remote.model

import com.google.gson.annotations.SerializedName

/** Request body for login API. */
data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

/** Request body for sign up / register API. */
data class SignUpRequest(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("address") val address: String? = null
)
