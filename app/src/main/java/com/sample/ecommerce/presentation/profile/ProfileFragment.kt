package com.sample.ecommerce.presentation.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sample.ecommerce.databinding.FragmentProfileBinding
import com.sample.ecommerce.presentation.auth.AuthHostActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels {
        val app = requireContext().applicationContext as com.sample.ecommerce.EcommerceApplication
        ProfileViewModel.Factory(
            com.sample.ecommerce.domain.usecase.GetCurrentUserUseCase(app.userRepository),
            app.userRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadProfile()
        binding.logoutButton.setOnClickListener {
            viewModel.logout()
            startActivity(Intent(requireContext(), AuthHostActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            activity?.finish()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.user.collectLatest { user ->
                    user?.let {
                        binding.fullNameText.text = it.fullName
                        binding.emailText.text = it.email
                        binding.phoneText.text = it.phone ?: "—"
                        binding.phoneText.visibility = View.VISIBLE
                        binding.phoneLabel.visibility = View.VISIBLE
                        binding.addressText.text = it.address ?: "—"
                        binding.addressText.visibility = View.VISIBLE
                        binding.addressLabel.visibility = View.VISIBLE
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
