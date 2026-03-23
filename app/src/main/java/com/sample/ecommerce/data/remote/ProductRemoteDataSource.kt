package com.sample.ecommerce.data.remote

import com.sample.ecommerce.data.remote.model.CategoryDto
import com.sample.ecommerce.data.remote.model.ProductDto
import retrofit2.HttpException
import java.io.IOException

/**
 * Remote data source for products API. Wraps [ProductApiService] and maps responses to [Result].
 * Currently unused; data is provided by [ProductLocalDataSource].
 */
class ProductRemoteDataSource(
    private val productApiService: ProductApiService = RetrofitClient.productApiService
) {

    suspend fun getCategories(): Result<List<CategoryDto>> = runCatching {
        val response = productApiService.getCategories()
        if (response.success && response.data != null) response.data
        else throw ApiException(response.code, response.message)
    }.recoverCatching { e -> mapToApiException(e) }

    suspend fun getProductsByCategory(categoryId: Long): Result<List<ProductDto>> = runCatching {
        val response = productApiService.getProducts(categoryId)
        if (response.success && response.data != null) response.data
        else throw ApiException(response.code, response.message)
    }.recoverCatching { e -> mapToApiException(e) }

    suspend fun getProductById(productId: Long): Result<ProductDto?> = runCatching {
        val response = productApiService.getProductById(productId)
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
