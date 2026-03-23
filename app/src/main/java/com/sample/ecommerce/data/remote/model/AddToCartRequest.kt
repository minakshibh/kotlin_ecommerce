package com.sample.ecommerce.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Request body for add-to-cart API.
 */
data class AddToCartRequest(
    @SerializedName("user_id") val userId: Long,
    @SerializedName("product_id") val productId: Long,
    @SerializedName("quantity") val quantity: Int
)
