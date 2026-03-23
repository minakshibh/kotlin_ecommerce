package com.sample.ecommerce.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Generic API response wrapper matching the server format.
 * Success: success=true, data non-null
 * Error: success=false, message contains error description, data is null
 */
data class ApiResponse<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: T? = null
)
