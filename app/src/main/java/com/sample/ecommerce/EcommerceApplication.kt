package com.sample.ecommerce

import com.sample.ecommerce.data.local.AppDatabase
import com.sample.ecommerce.data.local.DatabaseSeeder
import com.sample.ecommerce.data.local.SessionManager
import com.sample.ecommerce.data.repository.CartRepositoryImpl
import com.sample.ecommerce.data.repository.OrderRepositoryImpl
import com.sample.ecommerce.data.repository.ProductRepositoryImpl
import com.sample.ecommerce.data.repository.UserRepositoryImpl
import com.sample.ecommerce.domain.repository.CartRepository
import com.sample.ecommerce.domain.repository.OrderRepository
import com.sample.ecommerce.domain.repository.ProductRepository
import com.sample.ecommerce.domain.repository.UserRepository

class EcommerceApplication : android.app.Application() {

    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
    private val sessionManager: SessionManager by lazy { SessionManager(this) }

    val userRepository: UserRepository by lazy {
        UserRepositoryImpl(database, sessionManager)
    }
    val productRepository: ProductRepository by lazy {
        ProductRepositoryImpl(database)
    }
    val cartRepository: CartRepository by lazy {
        CartRepositoryImpl(database)
    }
    val orderRepository: OrderRepository by lazy {
        OrderRepositoryImpl(database)
    }

    override fun onCreate() {
        super.onCreate()
        DatabaseSeeder.seedIfNeeded(this)
    }
}
