package com.sample.ecommerce.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Product data as returned by the products API.
 */
data class ProductDto(
    @SerializedName("id") val id: Long,
    @SerializedName("category_id") val categoryId: Long,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Double,
    @SerializedName("image_url") val imageUrl: String? = null
)
