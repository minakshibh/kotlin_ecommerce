package com.sample.ecommerce.domain.usecase

import com.sample.ecommerce.domain.model.User
import com.sample.ecommerce.domain.repository.UserRepository

class SignUpUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(
        email: String,
        password: String,
        fullName: String,
        phone: String?,
        address: String?
    ): Result<Long> {
        if (email.isBlank() || password.isBlank() || fullName.isBlank()) {
            return Result.failure(IllegalArgumentException("Email, password and full name are required"))
        }
        val user = User(
            id = 0L,
            email = email.trim(),
            password = password,
            fullName = fullName.trim(),
            phone = phone?.takeIf { it.isNotBlank() },
            address = address?.takeIf { it.isNotBlank() }
        )
        return userRepository.signUp(user)
    }
}
