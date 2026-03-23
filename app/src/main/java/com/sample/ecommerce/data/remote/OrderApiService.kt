package com.sample.ecommerce.data.remote

import com.sample.ecommerce.data.remote.model.ApiResponse
import com.sample.ecommerce.data.remote.model.OrderDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Order history API endpoints.
 * Data is currently served from local DB via [OrderLocalDataSource].
 */
interface OrderApiService {

    @GET("orders")
    suspend fun getOrders(@Query("user_id") userId: Long): ApiResponse<List<OrderDto>>

    @GET("orders/{id}")
    suspend fun getOrderById(@Path("id") orderId: Long): ApiResponse<OrderDto>
}
