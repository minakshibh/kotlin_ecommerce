package com.sample.ecommerce.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sample.ecommerce.data.local.entity.OrderEntity
import com.sample.ecommerce.data.local.entity.OrderItemEntity

@Dao
interface OrderDao {
    @Insert
    suspend fun insertOrder(order: OrderEntity): Long

    @Insert
    suspend fun insertOrderItems(items: List<OrderItemEntity>)

    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY createdAt DESC")
    suspend fun getOrdersByUserId(userId: Long): List<OrderEntity>

    @Query("SELECT * FROM orders WHERE id = :orderId LIMIT 1")
    suspend fun getOrderById(orderId: Long): OrderEntity?

    @Query("SELECT * FROM order_items WHERE orderId = :orderId ORDER BY id")
    suspend fun getOrderItems(orderId: Long): List<OrderItemEntity>
}
