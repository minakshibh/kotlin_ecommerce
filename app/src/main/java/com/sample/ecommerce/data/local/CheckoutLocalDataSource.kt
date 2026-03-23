package com.sample.ecommerce.data.local

import com.sample.ecommerce.data.local.entity.OrderItemEntity
import com.sample.ecommerce.data.remote.model.CreateOrderResponseData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Provides checkout (create order) from local DB in the same shape as the API ([CreateOrderResponseData])
 * so the app can use local storage when the API is unavailable.
 */
class CheckoutLocalDataSource(
    private val database: AppDatabase
) {
    private val orderDao = database.orderDao()
    private val cartDao = database.cartDao()

    suspend fun createOrder(userId: Long): Result<CreateOrderResponseData> = withContext(Dispatchers.IO) {
        runCatching {
            val cartItems = cartDao.getByUserId(userId)
            if (cartItems.isEmpty()) throw LocalCheckoutException("Cart is empty")
            val totalAmount = cartItems.sumOf { it.productPrice * it.quantity }
            val order = com.sample.ecommerce.data.local.entity.OrderEntity(
                userId = userId,
                createdAt = System.currentTimeMillis(),
                totalAmount = totalAmount
            )
            val orderId = orderDao.insertOrder(order)
            val orderItems = cartItems.map { cart ->
                OrderItemEntity(
                    orderId = orderId,
                    productId = cart.productId,
                    productName = cart.productName,
                    quantity = cart.quantity,
                    unitPrice = cart.productPrice
                )
            }
            orderDao.insertOrderItems(orderItems)
            cartDao.deleteByUserId(userId)
            CreateOrderResponseData(orderId = orderId)
        }.recoverCatching { e ->
            if (e is LocalCheckoutException) throw e
            throw LocalCheckoutException(e.message ?: "Checkout failed")
        }
    }
}

class LocalCheckoutException(override val message: String) : Exception(message)
