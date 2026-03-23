package com.sample.ecommerce.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sample.ecommerce.domain.model.User
import com.sample.ecommerce.domain.repository.UserRepository
import com.sample.ecommerce.domain.usecase.GetCurrentUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    fun loadProfile() {
        viewModelScope.launch {
            _user.value = getCurrentUserUseCase()
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    class Factory(
        private val getCurrentUserUseCase: GetCurrentUserUseCase,
        private val userRepository: UserRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProfileViewModel(getCurrentUserUseCase, userRepository) as T
        }
    }
}
