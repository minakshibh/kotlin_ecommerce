package com.sample.ecommerce.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Order item data as returned by the orders API.
 */
data class OrderItemDto(
    @SerializedName("id") val id: Long,
    @SerializedName("order_id") val orderId: Long,
    @SerializedName("product_id") val productId: Long,
    @SerializedName("product_name") val productName: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("unit_price") val unitPrice: Double
)
