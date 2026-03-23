package com.sample.ecommerce.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Order data as returned by the orders API (order history).
 */
data class OrderDto(
    @SerializedName("id") val id: Long,
    @SerializedName("user_id") val userId: Long,
    @SerializedName("created_at") val createdAt: Long,
    @SerializedName("total_amount") val totalAmount: Double,
    @SerializedName("items") val items: List<OrderItemDto> = emptyList()
)
