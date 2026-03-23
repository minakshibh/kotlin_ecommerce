package com.sample.ecommerce.data.repository

import com.sample.ecommerce.data.local.AppDatabase
import com.sample.ecommerce.data.local.ProductLocalDataSource
import com.sample.ecommerce.domain.model.Category
import com.sample.ecommerce.domain.model.Product
import com.sample.ecommerce.domain.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Product repository. Uses [ProductLocalDataSource] only (local DB).
 * API structure exists in [ProductApiService] / [ProductRemoteDataSource] for future use.
 */
class ProductRepositoryImpl(
    private val database: AppDatabase,
    private val productLocalDataSource: ProductLocalDataSource = ProductLocalDataSource(database)
) : ProductRepository {

    override suspend fun getCategories(): List<Category> = withContext(Dispatchers.IO) {
        productLocalDataSource.getCategories().getOrThrow().map { dto ->
            Category(id = dto.id, name = dto.name)
        }
    }

    override suspend fun getProductsByCategory(categoryId: Long): List<Product> = withContext(Dispatchers.IO) {
        productLocalDataSource.getProductsByCategory(categoryId).getOrThrow().map { dto ->
            Product(
                id = dto.id,
                categoryId = dto.categoryId,
                name = dto.name,
                description = dto.description,
                price = dto.price,
                imageUrl = dto.imageUrl
            )
        }
    }

    override suspend fun getProductById(productId: Long): Product? = withContext(Dispatchers.IO) {
        productLocalDataSource.getProductById(productId).getOrNull()?.let { dto ->
            Product(
                id = dto.id,
                categoryId = dto.categoryId,
                name = dto.name,
                description = dto.description,
                price = dto.price,
                imageUrl = dto.imageUrl
            )
        }
    }
}
