package com.sample.ecommerce.domain.repository

import com.sample.ecommerce.domain.model.Order

interface OrderRepository {
    suspend fun getOrdersByUserId(userId: Long): List<Order>
    suspend fun getOrderById(orderId: Long): Order?
    suspend fun createOrderFromCart(userId: Long): Long
}
