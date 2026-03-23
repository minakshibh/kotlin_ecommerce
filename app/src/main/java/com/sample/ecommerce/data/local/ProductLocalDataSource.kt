package com.sample.ecommerce.data.local

import com.sample.ecommerce.data.local.entity.ProductEntity
import com.sample.ecommerce.data.remote.model.CategoryDto
import com.sample.ecommerce.data.remote.model.ProductDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Provides products and categories from local DB in the same shape as the API ([CategoryDto], [ProductDto])
 * so the app can use local storage when the API is unavailable.
 */
class ProductLocalDataSource(
    private val database: AppDatabase
) {
    private val categoryDao = database.categoryDao()
    private val productDao = database.productDao()

    suspend fun getCategories(): Result<List<CategoryDto>> = withContext(Dispatchers.IO) {
        runCatching {
            categoryDao.getAll().map { CategoryDto(id = it.id, name = it.name) }
        }
    }

    suspend fun getProductsByCategory(categoryId: Long): Result<List<ProductDto>> = withContext(Dispatchers.IO) {
        runCatching {
            productDao.getByCategoryId(categoryId).map { it.toDto() }
        }
    }

    suspend fun getProductById(productId: Long): Result<ProductDto?> = withContext(Dispatchers.IO) {
        runCatching {
            productDao.getById(productId)?.toDto()
        }
    }

    private fun ProductEntity.toDto() = ProductDto(
        id = id,
        categoryId = categoryId,
        name = name,
        description = description,
        price = price,
        imageUrl = imageUrl
    )
}
