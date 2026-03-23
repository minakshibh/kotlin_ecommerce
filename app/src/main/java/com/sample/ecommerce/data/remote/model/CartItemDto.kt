package com.sample.ecommerce.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Cart item data as returned by the cart API.
 */
data class CartItemDto(
    @SerializedName("id") val id: Long,
    @SerializedName("user_id") val userId: Long,
    @SerializedName("product_id") val productId: Long,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("product_name") val productName: String,
    @SerializedName("product_price") val productPrice: Double,
    @SerializedName("product_description") val productDescription: String
)
