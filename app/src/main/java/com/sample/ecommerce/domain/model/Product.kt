package com.sample.ecommerce.domain.model

data class Product(
    val id: Long,
    val categoryId: Long,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String?
)
