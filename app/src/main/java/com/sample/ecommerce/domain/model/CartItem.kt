package com.sample.ecommerce.domain.model

data class CartItem(
    val id: Long,
    val userId: Long,
    val productId: Long,
    val quantity: Int,
    val productName: String,
    val productPrice: Double,
    val productDescription: String
)
