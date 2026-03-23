package com.sample.ecommerce.domain.usecase

import com.sample.ecommerce.domain.model.Product
import com.sample.ecommerce.domain.repository.ProductRepository

class GetProductByIdUseCase(private val productRepository: ProductRepository) {
    suspend operator fun invoke(productId: Long): Product? =
        productRepository.getProductById(productId)
}
