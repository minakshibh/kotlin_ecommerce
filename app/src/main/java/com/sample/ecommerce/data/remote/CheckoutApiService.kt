package com.sample.ecommerce.data.remote

import com.sample.ecommerce.data.remote.model.ApiResponse
import com.sample.ecommerce.data.remote.model.CreateOrderRequest
import com.sample.ecommerce.data.remote.model.CreateOrderResponseData
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Checkout API endpoints (place order).
 * Data is currently served from local DB via [CheckoutLocalDataSource].
 */
interface CheckoutApiService {

    @POST("checkout/order")
    suspend fun createOrder(@Body request: CreateOrderRequest): ApiResponse<CreateOrderResponseData>
}
