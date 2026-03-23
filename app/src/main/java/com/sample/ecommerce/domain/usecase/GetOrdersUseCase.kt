package com.sample.ecommerce.domain.usecase

import com.sample.ecommerce.domain.model.Order
import com.sample.ecommerce.domain.repository.OrderRepository

class GetOrdersUseCase(private val orderRepository: OrderRepository) {
    suspend operator fun invoke(userId: Long): List<Order> =
        orderRepository.getOrdersByUserId(userId)
}

class CreateOrderFromCartUseCase(private val orderRepository: OrderRepository) {
    suspend operator fun invoke(userId: Long): Long =
        orderRepository.createOrderFromCart(userId)
}

class GetOrderByIdUseCase(private val orderRepository: OrderRepository) {
    suspend operator fun invoke(orderId: Long): Order? =
        orderRepository.getOrderById(orderId)
}
