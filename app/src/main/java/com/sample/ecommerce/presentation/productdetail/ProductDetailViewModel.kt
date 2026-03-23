package com.sample.ecommerce.presentation.productdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sample.ecommerce.domain.model.Product
import com.sample.ecommerce.domain.repository.UserRepository
import com.sample.ecommerce.domain.usecase.AddToCartUseCase
import com.sample.ecommerce.domain.usecase.GetCategoriesUseCase
import com.sample.ecommerce.domain.usecase.GetProductByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProductDetailState(
    val product: Product? = null,
    val categoryName: String = "",
    val quantity: Int = 1,
    val loading: Boolean = false,
    val addToCartMessage: String? = null
)

class ProductDetailViewModel(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProductDetailState())
    val state: StateFlow<ProductDetailState> = _state.asStateFlow()

    fun loadProduct(productId: Long) {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)
            val product = getProductByIdUseCase(productId)
            val categories = getCategoriesUseCase()
            val categoryName = product?.let { p ->
                categories.find { it.id == p.categoryId }?.name ?: ""
            } ?: ""
            _state.value = _state.value.copy(
                product = product,
                categoryName = categoryName,
                loading = false
            )
        }
    }

    fun setQuantity(quantity: Int) {
        _state.value = _state.value.copy(quantity = quantity.coerceAtLeast(0))
    }

    fun incrementQuantity() {
        setQuantity(_state.value.quantity + 1)
    }

    fun decrementQuantity() {
        setQuantity((_state.value.quantity - 1).coerceAtLeast(0))
    }

    fun addToCart() {
        viewModelScope.launch {
            val product = _state.value.product ?: return@launch
            val userId = userRepository.getCurrentUserId() ?: return@launch
            val quantity = _state.value.quantity
            if (quantity <= 0) {
                _state.value = _state.value.copy(addToCartMessage = "Please set quantity")
                return@launch
            }
            addToCartUseCase(userId, product.id, quantity)
            _state.value = _state.value.copy(addToCartMessage = "Added to cart!")
        }
    }

    fun clearAddToCartMessage() {
        _state.value = _state.value.copy(addToCartMessage = null)
    }

    class Factory(
        private val getProductByIdUseCase: GetProductByIdUseCase,
        private val getCategoriesUseCase: GetCategoriesUseCase,
        private val addToCartUseCase: AddToCartUseCase,
        private val userRepository: UserRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProductDetailViewModel(
                getProductByIdUseCase,
                getCategoriesUseCase,
                addToCartUseCase,
                userRepository
            ) as T
        }
    }
}
