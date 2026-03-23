package com.sample.ecommerce.presentation.auth

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
import com.sample.ecommerce.R
import com.sample.ecommerce.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
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
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text?.toString() ?: ""
            val password = binding.passwordEditText.text?.toString() ?: ""
            viewModel.login(email, password)
        }
        binding.signUpButton.setOnClickListener {
            (activity as? AuthNavigation)?.navigateToSignUp()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    binding.errorText.text = state.error
                    binding.errorText.visibility = if (state.error != null) View.VISIBLE else View.GONE
                    binding.loginButton.isEnabled = !state.loading
                    if (state.loading) {
                        binding.loginButton.text = "Loading..."
                    } else {
                        binding.loginButton.text = "Login"
                    }
                    if (state.loginSuccess) {
                        viewModel.resetLoginSuccess()
                        (activity as? AuthNavigation)?.onLoginSuccess()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface AuthNavigation {
        fun navigateToSignUp()
        fun onLoginSuccess()
    }
}
