package com.sample.ecommerce.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Response data for create order / checkout API.
 */
data class CreateOrderResponseData(
    @SerializedName("order_id") val orderId: Long
)
