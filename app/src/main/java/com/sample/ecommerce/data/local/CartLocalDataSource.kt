package com.sample.ecommerce.data.local

import com.sample.ecommerce.data.local.entity.CartItemEntity
import com.sample.ecommerce.data.remote.model.CartItemDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Provides cart data from local DB in the same shape as the API ([CartItemDto])
 * so the app can use local storage when the API is unavailable.
 */
class CartLocalDataSource(
    private val database: AppDatabase
) {
    private val cartDao = database.cartDao()
    private val productDao = database.productDao()

    suspend fun getCart(userId: Long): Result<List<CartItemDto>> = withContext(Dispatchers.IO) {
        runCatching {
            cartDao.getByUserId(userId).map { it.toDto() }
        }
    }

    suspend fun addToCart(userId: Long, productId: Long, quantity: Int): Result<CartItemDto> = withContext(Dispatchers.IO) {
        runCatching {
            val product = productDao.getById(productId) ?: throw LocalCartException("Product not found")
            val existing = cartDao.getByUserAndProduct(userId, productId)
            if (existing != null) {
                cartDao.updateQuantity(existing.id, existing.quantity + quantity)
                cartDao.getByUserId(userId).find { it.productId == productId }!!.toDto()
            } else {
                val entity = CartItemEntity(
                    userId = userId,
                    productId = productId,
                    quantity = quantity,
                    productName = product.name,
                    productPrice = product.price,
                    productDescription = product.description
                )
                val newId = cartDao.insert(entity)
                entity.copy(id = newId).toDto()
            }
        }.recoverCatching { e ->
            if (e is LocalCartException) throw e
            throw LocalCartException(e.message ?: "Add to cart failed")
        }
    }

    suspend fun updateQuantity(cartItemId: Long, quantity: Int): Result<CartItemDto> = withContext(Dispatchers.IO) {
        runCatching {
            cartDao.updateQuantity(cartItemId, quantity)
            cartDao.getById(cartItemId)?.toDto() ?: throw LocalCartException("Cart item not found")
        }.recoverCatching { e ->
            if (e is LocalCartException) throw e
            throw LocalCartException(e.message ?: "Update failed")
        }
    }

    suspend fun removeItem(cartItemId: Long): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            cartDao.deleteById(cartItemId)
        }
    }

    suspend fun clearCart(userId: Long): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            cartDao.deleteByUserId(userId)
        }
    }

    suspend fun getCartItemByUserAndProduct(userId: Long, productId: Long): CartItemDto? = withContext(Dispatchers.IO) {
        cartDao.getByUserAndProduct(userId, productId)?.toDto()
    }

    private fun CartItemEntity.toDto() = CartItemDto(
        id = id,
        userId = userId,
        productId = productId,
        quantity = quantity,
        productName = productName,
        productPrice = productPrice,
        productDescription = productDescription
    )
}

class LocalCartException(override val message: String) : Exception(message)
