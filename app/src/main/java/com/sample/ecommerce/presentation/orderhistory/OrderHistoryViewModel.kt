package com.sample.ecommerce.presentation.orderhistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sample.ecommerce.domain.model.Order
import com.sample.ecommerce.domain.repository.UserRepository
import com.sample.ecommerce.domain.usecase.GetOrdersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class OrderHistoryState(
    val orders: List<Order> = emptyList(),
    val loading: Boolean = false
)

class OrderHistoryViewModel(
    private val userRepository: UserRepository,
    private val getOrdersUseCase: GetOrdersUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(OrderHistoryState())
    val state: StateFlow<OrderHistoryState> = _state.asStateFlow()

    fun loadOrders() {
        viewModelScope.launch {
            val userId = userRepository.getCurrentUserId() ?: return@launch
            _state.value = _state.value.copy(loading = true)
            val orders = getOrdersUseCase(userId)
            _state.value = _state.value.copy(
                orders = orders,
                loading = false
            )
        }
    }

    class Factory(
        private val userRepository: UserRepository,
        private val getOrdersUseCase: GetOrdersUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return OrderHistoryViewModel(userRepository, getOrdersUseCase) as T
        }
    }
}
