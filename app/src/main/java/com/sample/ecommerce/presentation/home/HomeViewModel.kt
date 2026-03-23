package com.sample.ecommerce.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sample.ecommerce.domain.model.Category
import com.sample.ecommerce.domain.model.Product
import com.sample.ecommerce.domain.repository.UserRepository
import com.sample.ecommerce.domain.usecase.AddToCartUseCase
import com.sample.ecommerce.domain.usecase.GetCategoriesUseCase
import com.sample.ecommerce.domain.usecase.GetProductsByCategoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeState(
    val categories: List<Category> = emptyList(),
    val productsByCategory: Map<Long, List<Product>> = emptyMap(),
    val quantities: Map<Long, Int> = emptyMap(),
    val loading: Boolean = false,
    val addToCartMessage: String? = null
)

class HomeViewModel(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)
            val categories = getCategoriesUseCase()
            val productsByCategory = mutableMapOf<Long, List<Product>>()
            val quantities = _state.value.quantities.toMutableMap()
            for (cat in categories) {
                val products = getProductsByCategoryUseCase(cat.id)
                productsByCategory[cat.id] = products
                products.forEach { quantities[it.id] = quantities.getOrDefault(it.id, 0) }
            }
            _state.value = _state.value.copy(
                categories = categories,
                productsByCategory = productsByCategory,
                quantities = quantities,
                loading = false
            )
        }
    }

    private fun loadProducts(categoryId: Long) {
        // Used only for refresh if needed
        viewModelScope.launch {
            val products = getProductsByCategoryUseCase(categoryId)
            _state.value = _state.value.copy(
                productsByCategory = _state.value.productsByCategory + (categoryId to products)
            )
        }
    }

    fun setQuantity(productId: Long, quantity: Int) {
        val q = quantity.coerceAtLeast(0)
        _state.value = _state.value.copy(
            quantities = _state.value.quantities + (productId to q)
        )
    }

    fun incrementQuantity(productId: Long) {
        val current = _state.value.quantities[productId] ?: 0
        setQuantity(productId, current + 1)
    }

    fun decrementQuantity(productId: Long) {
        val current = _state.value.quantities[productId] ?: 0
        setQuantity(productId, (current - 1).coerceAtLeast(0))
    }

    fun getQuantity(productId: Long): Int = _state.value.quantities[productId] ?: 0

    fun addToCart(productId: Long) {
        viewModelScope.launch {
            val userId = userRepository.getCurrentUserId() ?: return@launch
            val quantity = _state.value.quantities[productId] ?: 1
            if (quantity <= 0) {
                _state.value = _state.value.copy(addToCartMessage = "Please set quantity")
                return@launch
            }
            addToCartUseCase(userId, productId, quantity)
            _state.value = _state.value.copy(
                addToCartMessage = "Added to cart!",
                quantities = _state.value.quantities + (productId to 0)
            )
        }
    }

    fun clearAddToCartMessage() {
        _state.value = _state.value.copy(addToCartMessage = null)
    }

    class Factory(
        private val getCategoriesUseCase: GetCategoriesUseCase,
        private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase,
        private val addToCartUseCase: AddToCartUseCase,
        private val userRepository: UserRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(
                getCategoriesUseCase,
                getProductsByCategoryUseCase,
                addToCartUseCase,
                userRepository
            ) as T
        }
    }
}
