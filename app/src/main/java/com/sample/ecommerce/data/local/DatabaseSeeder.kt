package com.sample.ecommerce.data.local

import android.content.Context
import com.sample.ecommerce.data.local.entity.CategoryEntity
import com.sample.ecommerce.data.local.entity.ProductEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object DatabaseSeeder {

    fun seedIfNeeded(context: Context) {
        val db = AppDatabase.getInstance(context)
        CoroutineScope(Dispatchers.IO).launch {
            val count = db.categoryDao().getAll()
            if (count.isEmpty()) {
                seedCategoriesAndProducts(db)
            }
        }
    }

    private suspend fun seedCategoriesAndProducts(db: AppDatabase) {
        val categoryDao = db.categoryDao()
        val productDao = db.productDao()

        val electronics = CategoryEntity(name = "Electronics")
        val fashion = CategoryEntity(name = "Fashion")
        val home = CategoryEntity(name = "Home & Garden")

        categoryDao.insertAll(listOf(electronics, fashion, home))

        val categories = categoryDao.getAll()
        val electronicsId = categories.find { it.name == "Electronics" }!!.id
        val fashionId = categories.find { it.name == "Fashion" }!!.id
        val homeId = categories.find { it.name == "Home & Garden" }!!.id

        val products = listOf(
            ProductEntity(categoryId = electronicsId, name = "Smartphone", description = "Latest smartphone with great camera", price = 299.99, imageUrl = null),
            ProductEntity(categoryId = electronicsId, name = "Laptop", description = "Portable laptop for work and play", price = 799.99, imageUrl = null),
            ProductEntity(categoryId = electronicsId, name = "Headphones", description = "Wireless noise-cancelling headphones", price = 149.99, imageUrl = null),
            ProductEntity(categoryId = electronicsId, name = "Smart Watch", description = "Fitness and notifications", price = 199.99, imageUrl = null),
            ProductEntity(categoryId = fashionId, name = "T-Shirt", description = "Cotton casual t-shirt", price = 24.99, imageUrl = null),
            ProductEntity(categoryId = fashionId, name = "Jeans", description = "Classic blue denim jeans", price = 59.99, imageUrl = null),
            ProductEntity(categoryId = fashionId, name = "Sneakers", description = "Comfortable running sneakers", price = 89.99, imageUrl = null),
            ProductEntity(categoryId = fashionId, name = "Jacket", description = "Lightweight jacket", price = 79.99, imageUrl = null),
            ProductEntity(categoryId = homeId, name = "Desk Lamp", description = "LED desk lamp with dimmer", price = 39.99, imageUrl = null),
            ProductEntity(categoryId = homeId, name = "Coffee Maker", description = "Programmable coffee maker", price = 69.99, imageUrl = null),
            ProductEntity(categoryId = homeId, name = "Throw Pillow", description = "Decorative throw pillow", price = 19.99, imageUrl = null),
            ProductEntity(categoryId = homeId, name = "Plant Pot", description = "Ceramic plant pot set", price = 29.99, imageUrl = null)
        )
        productDao.insertAll(products)
    }
}
