package com.sample.ecommerce.domain.usecase

import com.sample.ecommerce.domain.model.CartItem
import com.sample.ecommerce.domain.model.Category
import com.sample.ecommerce.domain.model.Product
import com.sample.ecommerce.domain.repository.ProductRepository

class GetCategoriesUseCase(private val productRepository: ProductRepository) {
    suspend operator fun invoke(): List<Category> = productRepository.getCategories()
}

class GetProductsByCategoryUseCase(private val productRepository: ProductRepository) {
    suspend operator fun invoke(categoryId: Long): List<Product> =
        productRepository.getProductsByCategory(categoryId)
}
