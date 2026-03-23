package com.sample.ecommerce.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sample.ecommerce.databinding.FragmentSignUpBinding
import kotlinx.coroutines.launch

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels {
        val app = requireContext().applicationContext as com.sample.ecommerce.EcommerceApplication
        AuthViewModel.Factory(
            com.sample.ecommerce.domain.usecase.SignUpUseCase(app.userRepository),
            com.sample.ecommerce.domain.usecase.LoginUseCase(app.userRepository),
            app.userRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signUpButton.setOnClickListener {
            val fullName = binding.fullNameEditText.text?.toString() ?: ""
            val email = binding.emailEditText.text?.toString() ?: ""
            val password = binding.passwordEditText.text?.toString() ?: ""
            val phone = binding.phoneEditText.text?.toString()?.takeIf { it.isNotBlank() }
            val address = binding.addressEditText.text?.toString()?.takeIf { it.isNotBlank() }
            viewModel.signUp(email, password, fullName, phone, address)
        }
        binding.loginButton.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    binding.errorText.text = state.error
                    binding.errorText.visibility = if (state.error != null) View.VISIBLE else View.GONE
                    binding.signUpButton.isEnabled = !state.loading
                    if (state.loading) {
                        binding.signUpButton.text = "Creating account..."
                    } else {
                        binding.signUpButton.text = "Sign Up"
                    }
                    if (state.signUpSuccess) {
                        viewModel.resetSignUpSuccess()
                        (activity as? LoginFragment.AuthNavigation)?.onLoginSuccess()
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
