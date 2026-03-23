package com.sample.ecommerce.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Category data as returned by the products API (categories list).
 */
data class CategoryDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String
)
