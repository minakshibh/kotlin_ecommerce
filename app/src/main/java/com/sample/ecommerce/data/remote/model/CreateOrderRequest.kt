package com.sample.ecommerce.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Request body for create order / checkout API.
 */
data class CreateOrderRequest(
    @SerializedName("user_id") val userId: Long
)
