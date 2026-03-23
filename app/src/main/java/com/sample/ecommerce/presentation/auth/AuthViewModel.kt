package com.sample.ecommerce.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sample.ecommerce.domain.repository.UserRepository
import com.sample.ecommerce.domain.usecase.LoginUseCase
import com.sample.ecommerce.domain.usecase.SignUpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthState(
    val loading: Boolean = false,
    val error: String? = null,
    val signUpSuccess: Boolean = false,
    val loginSuccess: Boolean = false
)

class AuthViewModel(
    private val signUpUseCase: SignUpUseCase,
    private val loginUseCase: LoginUseCase,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    fun signUp(
        email: String,
        password: String,
        fullName: String,
        phone: String?,
        address: String?
    ) {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            signUpUseCase(email, password, fullName, phone, address)
                .onSuccess { userId ->
                    if (userId > 0) {
                        userRepository.setCurrentUserId(userId)
                        _state.value = _state.value.copy(
                            loading = false,
                            signUpSuccess = true,
                            error = null
                        )
                    }
                }
                .onFailure {
                    _state.value = _state.value.copy(
                        loading = false,
                        error = it.message ?: "Sign up failed"
                    )
                }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            loginUseCase(email, password)
                .onSuccess { userId ->
                    _state.value = _state.value.copy(
                        loading = false,
                        loginSuccess = userId != null,
                        error = if (userId == null) "Invalid email or password" else null
                    )
                }
                .onFailure {
                    _state.value = _state.value.copy(
                        loading = false,
                        error = it.message ?: "Login failed"
                    )
                }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun resetSignUpSuccess() {
        _state.value = _state.value.copy(signUpSuccess = false)
    }

    fun resetLoginSuccess() {
        _state.value = _state.value.copy(loginSuccess = false)
    }

    class Factory(
        private val signUpUseCase: SignUpUseCase,
        private val loginUseCase: LoginUseCase,
        private val userRepository: UserRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuthViewModel(signUpUseCase, loginUseCase, userRepository) as T
        }
    }
}
