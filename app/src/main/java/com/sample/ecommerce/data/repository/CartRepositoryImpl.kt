package com.sample.ecommerce.data.repository

import com.sample.ecommerce.data.local.AppDatabase
import com.sample.ecommerce.data.local.CartLocalDataSource
import com.sample.ecommerce.domain.model.CartItem
import com.sample.ecommerce.domain.repository.CartRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Cart repository. Uses [CartLocalDataSource] only (local DB).
 * API structure exists in [CartApiService] / [CartRemoteDataSource] for future use.
 */
class CartRepositoryImpl(
    private val database: AppDatabase,
    private val cartLocalDataSource: CartLocalDataSource = CartLocalDataSource(database)
) : CartRepository {

    override suspend fun getCartItems(userId: Long): List<CartItem> = withContext(Dispatchers.IO) {
        cartLocalDataSource.getCart(userId).getOrThrow().map { dto ->
            CartItem(
                id = dto.id,
                userId = dto.userId,
                productId = dto.productId,
                quantity = dto.quantity,
                productName = dto.productName,
                productPrice = dto.productPrice,
                productDescription = dto.productDescription
            )
        }
    }

    override suspend fun addToCart(userId: Long, productId: Long, quantity: Int) = withContext(Dispatchers.IO) {
        cartLocalDataSource.addToCart(userId, productId, quantity).getOrThrow()
    }

    override suspend fun updateCartItemQuantity(cartItemId: Long, quantity: Int) = withContext(Dispatchers.IO) {
        cartLocalDataSource.updateQuantity(cartItemId, quantity).getOrThrow()
    }

    override suspend fun removeCartItem(cartItemId: Long) = withContext(Dispatchers.IO) {
        cartLocalDataSource.removeItem(cartItemId).getOrThrow()
    }

    override suspend fun clearCart(userId: Long) = withContext(Dispatchers.IO) {
        cartLocalDataSource.clearCart(userId).getOrThrow()
    }

    override suspend fun getCartItemByUserAndProduct(userId: Long, productId: Long): CartItem? = withContext(Dispatchers.IO) {
        cartLocalDataSource.getCartItemByUserAndProduct(userId, productId)?.let { dto ->
            CartItem(
                id = dto.id,
                userId = dto.userId,
                productId = dto.productId,
                quantity = dto.quantity,
                productName = dto.productName,
                productPrice = dto.productPrice,
                productDescription = dto.productDescription
            )
        }
    }
}
