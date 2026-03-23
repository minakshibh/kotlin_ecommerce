package com.sample.ecommerce.presentation.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.sample.ecommerce.R
import com.sample.ecommerce.presentation.dashboard.DashboardActivity

class AuthHostActivity : AppCompatActivity(), LoginFragment.AuthNavigation {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_host)
        if (supportFragmentManager.findFragmentById(R.id.auth_container) == null) {
            supportFragmentManager.commit {
                replace(R.id.auth_container, LoginFragment())
            }
        }
    }

    override fun navigateToSignUp() {
        supportFragmentManager.commit {
            replace(R.id.auth_container, SignUpFragment())
            addToBackStack(null)
        }
    }

    override fun onLoginSuccess() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }
}
