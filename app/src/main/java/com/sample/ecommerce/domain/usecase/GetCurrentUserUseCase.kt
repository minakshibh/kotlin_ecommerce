package com.sample.ecommerce.domain.usecase

import com.sample.ecommerce.domain.repository.UserRepository

class GetCurrentUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(): com.sample.ecommerce.domain.model.User? {
        val userId = userRepository.getCurrentUserId() ?: return null
        return userRepository.getUserById(userId)
    }
}
