package com.sample.ecommerce.data.remote

import com.sample.ecommerce.data.remote.model.CreateOrderRequest
import com.sample.ecommerce.data.remote.model.CreateOrderResponseData
import retrofit2.HttpException
import java.io.IOException

/**
 * Remote data source for checkout API. Wraps [CheckoutApiService] and maps responses to [Result].
 * Currently unused; data is provided by [CheckoutLocalDataSource].
 */
class CheckoutRemoteDataSource(
    private val checkoutApiService: CheckoutApiService = RetrofitClient.checkoutApiService
) {

    suspend fun createOrder(userId: Long): Result<CreateOrderResponseData> = runCatching {
        val response = checkoutApiService.createOrder(CreateOrderRequest(userId))
        if (response.success && response.data != null) response.data
        else throw ApiException(response.code, response.message)
    }.recoverCatching { e ->
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
