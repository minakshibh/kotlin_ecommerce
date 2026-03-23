package com.sample.ecommerce.data.remote

import com.sample.ecommerce.data.remote.model.OrderDto
import retrofit2.HttpException
import java.io.IOException

/**
 * Remote data source for order history API. Wraps [OrderApiService] and maps responses to [Result].
 * Currently unused; data is provided by [OrderLocalDataSource].
 */
class OrderRemoteDataSource(
    private val orderApiService: OrderApiService = RetrofitClient.orderApiService
) {

    suspend fun getOrders(userId: Long): Result<List<OrderDto>> = runCatching {
        val response = orderApiService.getOrders(userId)
        if (response.success && response.data != null) response.data
        else throw ApiException(response.code, response.message)
    }.recoverCatching { e -> mapToApiException(e) }

    suspend fun getOrderById(orderId: Long): Result<OrderDto?> = runCatching {
        val response = orderApiService.getOrderById(orderId)
        if (response.success) response.data
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
