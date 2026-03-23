package com.sample.ecommerce.presentation.orderdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sample.ecommerce.domain.model.Order
import com.sample.ecommerce.domain.usecase.GetOrderByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class OrderDetailState(
    val order: Order? = null,
    val loading: Boolean = false
)

class OrderDetailViewModel(
    private val getOrderByIdUseCase: GetOrderByIdUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(OrderDetailState())
    val state: StateFlow<OrderDetailState> = _state.asStateFlow()

    fun loadOrder(orderId: Long) {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)
            val order = getOrderByIdUseCase(orderId)
            _state.value = _state.value.copy(
                order = order,
                loading = false
            )
        }
    }

    class Factory(
        private val getOrderByIdUseCase: GetOrderByIdUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return OrderDetailViewModel(getOrderByIdUseCase) as T
        }
    }
}
