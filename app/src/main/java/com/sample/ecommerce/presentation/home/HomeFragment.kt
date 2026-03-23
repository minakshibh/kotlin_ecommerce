package com.sample.ecommerce.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.ecommerce.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels {
        val app = requireContext().applicationContext as com.sample.ecommerce.EcommerceApplication
        HomeViewModel.Factory(
            com.sample.ecommerce.domain.usecase.GetCategoriesUseCase(app.productRepository),
            com.sample.ecommerce.domain.usecase.GetProductsByCategoryUseCase(app.productRepository),
            com.sample.ecommerce.domain.usecase.AddToCartUseCase(app.cartRepository),
            app.userRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    binding.progressBar.visibility = if (state.loading) View.VISIBLE else View.GONE
                    if (binding.categoriesRecyclerView.layoutManager == null) {
                        binding.categoriesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                    }
                    binding.categoriesRecyclerView.adapter = CategoryAdapter(
                        productsByCategory = state.productsByCategory,
                        quantities = state.quantities,
                        onQuantityPlus = viewModel::incrementQuantity,
                        onQuantityMinus = viewModel::decrementQuantity,
                        onAddToCart = viewModel::addToCart,
                        onProductClick = { productId ->
                            com.sample.ecommerce.presentation.productdetail.ProductDetailActivity.start(requireContext(), productId)
                        }
                    ).apply {
                        submitList(state.categories)
                    }
                    state.addToCartMessage?.let { msg ->
                        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                        viewModel.clearAddToCartMessage()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
