package com.sample.ecommerce.domain.model

data class User(
    val id: Long,
    val email: String,
    val password: String,
    val fullName: String,
    val phone: String?,
    val address: String?
)
