package com.sample.ecommerce.domain.model

data class Order(
    val id: Long,
    val userId: Long,
    val createdAt: Long,
    val totalAmount: Double,
    val items: List<OrderItem> = emptyList()
)

data class OrderItem(
    val id: Long,
    val orderId: Long,
    val productId: Long,
    val productName: String,
    val quantity: Int,
    val unitPrice: Double
)
