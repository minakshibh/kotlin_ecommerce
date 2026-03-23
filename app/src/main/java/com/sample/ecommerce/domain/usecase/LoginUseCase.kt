package com.sample.ecommerce.domain.usecase

import com.sample.ecommerce.domain.repository.UserRepository

class LoginUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(email: String, password: String): Result<Long?> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Email and password are required"))
        }
        return userRepository.login(email.trim(), password).map { it?.id }
    }
}
