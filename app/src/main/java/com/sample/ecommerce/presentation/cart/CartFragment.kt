package com.sample.ecommerce.presentation.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.ecommerce.databinding.FragmentCartBinding
import kotlinx.coroutines.launch

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CartViewModel by viewModels {
        val app = requireContext().applicationContext as com.sample.ecommerce.EcommerceApplication
        CartViewModel.Factory(
            app.userRepository,
            com.sample.ecommerce.domain.usecase.GetCartItemsUseCase(app.cartRepository),
            com.sample.ecommerce.domain.usecase.ClearCartUseCase(app.cartRepository),
            com.sample.ecommerce.domain.usecase.CreateOrderFromCartUseCase(app.orderRepository),
            com.sample.ecommerce.domain.usecase.UpdateCartItemQuantityUseCase(app.cartRepository),
            com.sample.ecommerce.domain.usecase.RemoveCartItemUseCase(app.cartRepository)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadCart()
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.checkoutButton.setOnClickListener {
            viewModel.checkout()
            (activity as? com.sample.ecommerce.presentation.dashboard.DashboardActivity)?.navigateToCheckoutSuccess()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    binding.progressBar.visibility = if (state.loading) View.VISIBLE else View.GONE
                    binding.totalText.text = "$${String.format("%.2f", state.total)}"
                    binding.cartRecyclerView.adapter = CartAdapter(
                        state.items,
                        onQuantityPlus = viewModel::incrementQuantity,
                        onQuantityMinus = viewModel::decrementQuantity
                    )
                    binding.checkoutButton.isEnabled = state.items.isNotEmpty()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadCart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
