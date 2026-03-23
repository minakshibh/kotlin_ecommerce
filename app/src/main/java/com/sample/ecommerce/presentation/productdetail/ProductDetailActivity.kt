package com.sample.ecommerce.presentation.productdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sample.ecommerce.R
import com.sample.ecommerce.databinding.ActivityProductDetailBinding
import kotlinx.coroutines.launch

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding

    private val viewModel: ProductDetailViewModel by viewModels {
        val app = application as com.sample.ecommerce.EcommerceApplication
        ProductDetailViewModel.Factory(
            com.sample.ecommerce.domain.usecase.GetProductByIdUseCase(app.productRepository),
            com.sample.ecommerce.domain.usecase.GetCategoriesUseCase(app.productRepository),
            com.sample.ecommerce.domain.usecase.AddToCartUseCase(app.cartRepository),
            app.userRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Product Details"

        val productId = intent.getLongExtra(EXTRA_PRODUCT_ID, -1L)
        if (productId < 0) {
            finish()
            return
        }
        viewModel.loadProduct(productId)

        binding.quantityPlus.setOnClickListener { viewModel.incrementQuantity() }
        binding.quantityMinus.setOnClickListener { viewModel.decrementQuantity() }
        binding.addToCartButton.setOnClickListener { viewModel.addToCart() }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    binding.progressBar.visibility = if (state.loading) View.VISIBLE else View.GONE
                    state.product?.let { product ->
                        binding.productName.text = product.name
                        binding.productDescription.text = product.description
                        binding.productPrice.text = "$${String.format("%.2f", product.price)}"
                        binding.categoryName.text = state.categoryName
                        binding.quantityText.text = state.quantity.toString()
                    }
                    binding.addToCartMessage.text = state.addToCartMessage
                    binding.addToCartMessage.visibility =
                        if (state.addToCartMessage != null) View.VISIBLE else View.GONE
                    state.addToCartMessage?.let {
                        viewModel.clearAddToCartMessage()
                        Toast.makeText(this@ProductDetailActivity, it, Toast.LENGTH_SHORT).show()
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
        private const val EXTRA_PRODUCT_ID = "product_id"
        fun start(context: Context, productId: Long) {
            context.startActivity(Intent(context, ProductDetailActivity::class.java).apply {
                putExtra(EXTRA_PRODUCT_ID, productId)
            })
        }
    }
}
