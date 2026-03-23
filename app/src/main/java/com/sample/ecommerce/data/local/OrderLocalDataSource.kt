package com.sample.ecommerce.data.local

import com.sample.ecommerce.data.local.entity.OrderItemEntity
import com.sample.ecommerce.data.remote.model.OrderDto
import com.sample.ecommerce.data.remote.model.OrderItemDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Provides order history from local DB in the same shape as the API ([OrderDto])
 * so the app can use local storage when the API is unavailable.
 */
class OrderLocalDataSource(
    private val database: AppDatabase
) {
    private val orderDao = database.orderDao()

    suspend fun getOrders(userId: Long): Result<List<OrderDto>> = withContext(Dispatchers.IO) {
        runCatching {
            orderDao.getOrdersByUserId(userId).map { entity ->
                val items = orderDao.getOrderItems(entity.id).map { it.toDto() }
                OrderDto(
                    id = entity.id,
                    userId = entity.userId,
                    createdAt = entity.createdAt,
                    totalAmount = entity.totalAmount,
                    items = items
                )
            }
        }
    }

    suspend fun getOrderById(orderId: Long): Result<OrderDto?> = withContext(Dispatchers.IO) {
        runCatching {
            orderDao.getOrderById(orderId)?.let { entity ->
                val items = orderDao.getOrderItems(entity.id).map { it.toDto() }
                OrderDto(
                    id = entity.id,
                    userId = entity.userId,
                    createdAt = entity.createdAt,
                    totalAmount = entity.totalAmount,
                    items = items
                )
            }
        }
    }

    private fun OrderItemEntity.toDto() = OrderItemDto(
        id = id,
        orderId = orderId,
        productId = productId,
        productName = productName,
        quantity = quantity,
        unitPrice = unitPrice
    )
}
