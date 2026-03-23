package com.sample.ecommerce.domain.usecase

import com.sample.ecommerce.domain.model.CartItem
import com.sample.ecommerce.domain.repository.CartRepository

class GetCartItemsUseCase(private val cartRepository: CartRepository) {
    suspend operator fun invoke(userId: Long): List<CartItem> =
        cartRepository.getCartItems(userId)
}

class AddToCartUseCase(private val cartRepository: CartRepository) {
    suspend operator fun invoke(userId: Long, productId: Long, quantity: Int) {
        cartRepository.addToCart(userId, productId, quantity)
    }
}

class ClearCartUseCase(private val cartRepository: CartRepository) {
    suspend operator fun invoke(userId: Long) {
        cartRepository.clearCart(userId)
    }
}

class UpdateCartItemQuantityUseCase(private val cartRepository: CartRepository) {
    suspend operator fun invoke(cartItemId: Long, quantity: Int) {
        cartRepository.updateCartItemQuantity(cartItemId, quantity)
    }
}

class RemoveCartItemUseCase(private val cartRepository: CartRepository) {
    suspend operator fun invoke(cartItemId: Long) {
        cartRepository.removeCartItem(cartItemId)
    }
}
