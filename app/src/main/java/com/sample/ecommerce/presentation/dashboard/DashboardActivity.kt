package com.sample.ecommerce.presentation.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.sample.ecommerce.R
import com.sample.ecommerce.databinding.ActivityDashboardBinding
import com.sample.ecommerce.presentation.checkout.CheckoutSuccessActivity
import com.sample.ecommerce.presentation.cart.CartFragment
import com.sample.ecommerce.presentation.home.HomeFragment
import com.sample.ecommerce.presentation.orderhistory.OrderHistoryFragment
import com.sample.ecommerce.presentation.profile.ProfileFragment

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = DashboardPagerAdapter(this)
        adapter.addFragment(HomeFragment(), "Home")
        adapter.addFragment(CartFragment(), "Cart")
        adapter.addFragment(OrderHistoryFragment(), "Orders")
        adapter.addFragment(ProfileFragment(), "Profile")
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 4

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = adapter.getPageTitle(position)
        }.attach()
    }

    fun navigateToCheckoutSuccess() {
        startActivity(Intent(this, CheckoutSuccessActivity::class.java))
        finish()
    }
}
