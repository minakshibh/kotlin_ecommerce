package com.sample.ecommerce.data.remote

import com.sample.ecommerce.data.remote.model.AddToCartRequest
import com.sample.ecommerce.data.remote.model.CartItemDto
import com.sample.ecommerce.data.remote.model.UpdateCartQuantityRequest
import retrofit2.HttpException
import java.io.IOException

/**
 * Remote data source for cart API. Wraps [CartApiService] and maps responses to [Result].
 * Currently unused; data is provided by [CartLocalDataSource].
 */
class CartRemoteDataSource(
    private val cartApiService: CartApiService = RetrofitClient.cartApiService
) {

    suspend fun getCart(userId: Long): Result<List<CartItemDto>> = runCatching {
        val response = cartApiService.getCart(userId)
        if (response.success && response.data != null) response.data
        else throw ApiException(response.code, response.message)
    }.recoverCatching { e -> mapToApiException(e) }

    suspend fun addToCart(userId: Long, productId: Long, quantity: Int): Result<CartItemDto> = runCatching {
        val response = cartApiService.addToCart(AddToCartRequest(userId, productId, quantity))
        if (response.success && response.data != null) response.data
        else throw ApiException(response.code, response.message)
    }.recoverCatching { e -> mapToApiException(e) }

    suspend fun updateQuantity(cartItemId: Long, quantity: Int): Result<CartItemDto> = runCatching {
        val response = cartApiService.updateQuantity(cartItemId, UpdateCartQuantityRequest(quantity))
        if (response.success && response.data != null) response.data
        else throw ApiException(response.code, response.message)
    }.recoverCatching { e -> mapToApiException(e) }

    suspend fun removeItem(cartItemId: Long): Result<Unit> = runCatching {
        val response = cartApiService.removeItem(cartItemId)
        if (response.success) Unit
        else throw ApiException(response.code, response.message)
    }.recoverCatching { e -> mapToApiException(e) }

    suspend fun clearCart(userId: Long): Result<Unit> = runCatching {
        val response = cartApiService.clearCart(userId)
        if (response.success) Unit
        else throw ApiException(response.code, response.message)
    }.recoverCatching { e -> mapToApiException(e) }

    private fun mapToApiException(e: Throwable): Nothing {
        when (e) {
            is ApiException -> throw e
            is HttpException -> throw ApiException(
                code = e.code(),
                message = e.response()?.errorBody()?.string() ?: e.message()
            )
            is IOException -> throw ApiException(-1, "Network error: ${e.message}")
            else -> throw ApiException(-1, e.message ?: "Unknown error")
        }
    }
}
