package com.sample.ecommerce.data.repository

import com.sample.ecommerce.data.local.AppDatabase
import com.sample.ecommerce.data.local.CheckoutLocalDataSource
import com.sample.ecommerce.data.local.OrderLocalDataSource
import com.sample.ecommerce.domain.model.Order
import com.sample.ecommerce.domain.model.OrderItem
import com.sample.ecommerce.domain.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Order repository. Uses [OrderLocalDataSource] and [CheckoutLocalDataSource] only (local DB).
 * API structure exists in [OrderApiService] / [CheckoutApiService] and their RemoteDataSources for future use.
 */
class OrderRepositoryImpl(
    private val database: AppDatabase,
    private val orderLocalDataSource: OrderLocalDataSource = OrderLocalDataSource(database),
    private val checkoutLocalDataSource: CheckoutLocalDataSource = CheckoutLocalDataSource(database)
) : OrderRepository {

    override suspend fun getOrdersByUserId(userId: Long): List<Order> = withContext(Dispatchers.IO) {
        orderLocalDataSource.getOrders(userId).getOrThrow().map { dto ->
            Order(
                id = dto.id,
                userId = dto.userId,
                createdAt = dto.createdAt,
                totalAmount = dto.totalAmount,
                items = dto.items.map { item ->
                    OrderItem(
                        id = item.id,
                        orderId = item.orderId,
                        productId = item.productId,
                        productName = item.productName,
                        quantity = item.quantity,
                        unitPrice = item.unitPrice
                    )
                }
            )
        }
    }

    override suspend fun getOrderById(orderId: Long): Order? = withContext(Dispatchers.IO) {
        orderLocalDataSource.getOrderById(orderId).getOrNull()?.let { dto ->
            Order(
                id = dto.id,
                userId = dto.userId,
                createdAt = dto.createdAt,
                totalAmount = dto.totalAmount,
                items = dto.items.map { item ->
                    OrderItem(
                        id = item.id,
                        orderId = item.orderId,
                        productId = item.productId,
                        productName = item.productName,
                        quantity = item.quantity,
                        unitPrice = item.unitPrice
                    )
                }
            )
        }
    }

    override suspend fun createOrderFromCart(userId: Long): Long = withContext(Dispatchers.IO) {
        checkoutLocalDataSource.createOrder(userId).getOrThrow().orderId
    }
}
