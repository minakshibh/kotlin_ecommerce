package com.sample.ecommerce.data.remote

import com.sample.ecommerce.data.remote.model.AddToCartRequest
import com.sample.ecommerce.data.remote.model.ApiResponse
import com.sample.ecommerce.data.remote.model.CartItemDto
import com.sample.ecommerce.data.remote.model.UpdateCartQuantityRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Cart API endpoints.
 * Data is currently served from local DB via [CartLocalDataSource].
 */
interface CartApiService {

    @GET("cart")
    suspend fun getCart(@Query("user_id") userId: Long): ApiResponse<List<CartItemDto>>

    @PUT("cart/add")
    suspend fun addToCart(@Body request: AddToCartRequest): ApiResponse<CartItemDto>

    @PUT("cart/{id}")
    suspend fun updateQuantity(
        @Path("id") cartItemId: Long,
        @Body request: UpdateCartQuantityRequest
    ): ApiResponse<CartItemDto>

    @DELETE("cart/{id}")
    suspend fun removeItem(@Path("id") cartItemId: Long): ApiResponse<Unit>

    @DELETE("cart/clear")
    suspend fun clearCart(@Query("user_id") userId: Long): ApiResponse<Unit>
}
