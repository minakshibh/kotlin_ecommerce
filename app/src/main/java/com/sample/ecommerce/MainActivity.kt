package com.sample.ecommerce

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sample.ecommerce.presentation.auth.AuthHostActivity
import com.sample.ecommerce.presentation.dashboard.DashboardActivity
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as EcommerceApplication
        val userId = runBlocking { app.userRepository.getCurrentUserId() }
        if (userId != null) {
            startActivity(Intent(this, DashboardActivity::class.java))
        } else {
            startActivity(Intent(this, AuthHostActivity::class.java))
        }
        finish()
    }
}
