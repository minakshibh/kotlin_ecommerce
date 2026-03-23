package com.sample.ecommerce.domain.repository

import com.sample.ecommerce.data.remote.model.CartItemDto
import com.sample.ecommerce.domain.model.CartItem
import com.sample.ecommerce.domain.model.Category
import com.sample.ecommerce.domain.model.Product

interface ProductRepository {
    suspend fun getCategories(): List<Category>
    suspend fun getProductsByCategory(categoryId: Long): List<Product>
    suspend fun getProductById(productId: Long): Product?
}

interface CartRepository {
    suspend fun getCartItems(userId: Long): List<CartItem>
    suspend fun addToCart(userId: Long, productId: Long, quantity: Int): CartItemDto
    suspend fun updateCartItemQuantity(cartItemId: Long, quantity: Int): CartItemDto
    suspend fun removeCartItem(cartItemId: Long)
    suspend fun clearCart(userId: Long)
    suspend fun getCartItemByUserAndProduct(userId: Long, productId: Long): CartItem?
}
