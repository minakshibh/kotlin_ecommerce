package com.sample.ecommerce.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Request body for update cart item quantity API.
 */
data class UpdateCartQuantityRequest(
    @SerializedName("quantity") val quantity: Int
)
