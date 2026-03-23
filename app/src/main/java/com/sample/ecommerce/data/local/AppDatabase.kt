package com.sample.ecommerce.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sample.ecommerce.data.local.dao.CartDao
import com.sample.ecommerce.data.local.dao.CategoryDao
import com.sample.ecommerce.data.local.dao.OrderDao
import com.sample.ecommerce.data.local.dao.ProductDao
import com.sample.ecommerce.data.local.dao.UserDao
import com.sample.ecommerce.data.local.entity.CartItemEntity
import com.sample.ecommerce.data.local.entity.CategoryEntity
import com.sample.ecommerce.data.local.entity.OrderEntity
import com.sample.ecommerce.data.local.entity.OrderItemEntity
import com.sample.ecommerce.data.local.entity.ProductEntity
import com.sample.ecommerce.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        CategoryEntity::class,
        ProductEntity::class,
        CartItemEntity::class,
        OrderEntity::class,
        OrderItemEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao

    companion object {
        private const val DATABASE_NAME = "ecommerce.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
