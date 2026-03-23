package com.sample.ecommerce.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sample.ecommerce.domain.model.CartItem
import com.sample.ecommerce.domain.repository.CartRepository
import com.sample.ecommerce.domain.repository.UserRepository
import com.sample.ecommerce.domain.usecase.ClearCartUseCase
import com.sample.ecommerce.domain.usecase.CreateOrderFromCartUseCase
import com.sample.ecommerce.domain.usecase.GetCartItemsUseCase
import com.sample.ecommerce.domain.usecase.RemoveCartItemUseCase
import com.sample.ecommerce.domain.usecase.UpdateCartItemQuantityUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CartState(
    val items: List<CartItem> = emptyList(),
    val loading: Boolean = false,
    val total: Double = 0.0
)

class CartViewModel(
    private val userRepository: UserRepository,
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val clearCartUseCase: ClearCartUseCase,
    private val createOrderFromCartUseCase: CreateOrderFromCartUseCase,
    private val updateCartItemQuantityUseCase: UpdateCartItemQuantityUseCase,
    private val removeCartItemUseCase: RemoveCartItemUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CartState())
    val state: StateFlow<CartState> = _state.asStateFlow()

    fun loadCart() {
        viewModelScope.launch {
            val userId = userRepository.getCurrentUserId() ?: return@launch
            _state.value = _state.value.copy(loading = true)
            val items = getCartItemsUseCase(userId)
            val total = items.sumOf { it.productPrice * it.quantity }
            _state.value = _state.value.copy(
                items = items,
                total = total,
                loading = false
            )
        }
    }

    fun checkout() {
        viewModelScope.launch {
            val userId = userRepository.getCurrentUserId() ?: return@launch
            createOrderFromCartUseCase(userId)
        }
    }

    fun updateQuantity(cartItemId: Long, newQuantity: Int) {
        viewModelScope.launch {
            if (newQuantity <= 0) {
                removeCartItemUseCase(cartItemId)
            } else {
                updateCartItemQuantityUseCase(cartItemId, newQuantity)
            }
            loadCart()
        }
    }

    fun incrementQuantity(item: CartItem) {
        updateQuantity(item.id, item.quantity + 1)
    }

    fun decrementQuantity(item: CartItem) {
        updateQuantity(item.id, (item.quantity - 1).coerceAtLeast(0))
    }

    class Factory(
        private val userRepository: UserRepository,
        private val getCartItemsUseCase: GetCartItemsUseCase,
        private val clearCartUseCase: ClearCartUseCase,
        private val createOrderFromCartUseCase: CreateOrderFromCartUseCase,
        private val updateCartItemQuantityUseCase: UpdateCartItemQuantityUseCase,
        private val removeCartItemUseCase: RemoveCartItemUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CartViewModel(
                userRepository,
                getCartItemsUseCase,
                clearCartUseCase,
                createOrderFromCartUseCase,
                updateCartItemQuantityUseCase,
                removeCartItemUseCase
            ) as T
        }
    }
}
