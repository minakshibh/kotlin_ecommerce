package com.sample.ecommerce.presentation.orderdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.ecommerce.databinding.ActivityOrderDetailBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderDetailBinding
    private val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())

    private val viewModel: OrderDetailViewModel by viewModels {
        val app = application as com.sample.ecommerce.EcommerceApplication
        OrderDetailViewModel.Factory(
            com.sample.ecommerce.domain.usecase.GetOrderByIdUseCase(app.orderRepository)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Order Details"

        val orderId = intent.getLongExtra(EXTRA_ORDER_ID, -1L)
        if (orderId < 0) {
            finish()
            return
        }
        viewModel.loadOrder(orderId)

        binding.orderItemsRecyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    binding.progressBar.visibility = if (state.loading) View.VISIBLE else View.GONE
                    state.order?.let { order ->
                        binding.orderIdText.text = "Order #${order.id}"
                        binding.orderDateText.text = dateFormat.format(Date(order.createdAt))
                        binding.orderTotalText.text = "$${String.format("%.2f", order.totalAmount)}"
                        binding.orderItemsRecyclerView.adapter = OrderDetailItemsAdapter(order.items)
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        private const val EXTRA_ORDER_ID = "order_id"
        fun start(context: Context, orderId: Long) {
            context.startActivity(Intent(context, OrderDetailActivity::class.java).apply {
                putExtra(EXTRA_ORDER_ID, orderId)
            })
        }
    }
}
