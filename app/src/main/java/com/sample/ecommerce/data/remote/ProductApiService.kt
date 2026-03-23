package com.sample.ecommerce.data.remote

import com.sample.ecommerce.data.remote.model.ApiResponse
import com.sample.ecommerce.data.remote.model.CategoryDto
import com.sample.ecommerce.data.remote.model.ProductDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Products API endpoints.
 * Data is currently served from local DB via [ProductLocalDataSource].
 */
interface ProductApiService {

    @GET("products/categories")
    suspend fun getCategories(): ApiResponse<List<CategoryDto>>

    @GET("products")
    suspend fun getProducts(@Query("category_id") categoryId: Long): ApiResponse<List<ProductDto>>

    @GET("products/{id}")
    suspend fun getProductById(@retrofit2.http.Path("id") productId: Long): ApiResponse<ProductDto>
}
