package com.alisafdar.enocassessment.ui.auth

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.alisafdar.enocassessment.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.alisafdar.enocassessment.data.network.Resource
import com.alisafdar.enocassessment.databinding.FragmentLoginBinding
import com.alisafdar.enocassessment.utils.enable
import com.alisafdar.enocassessment.utils.handleApiError
import com.alisafdar.enocassessment.ui.home.HomeActivity
import com.alisafdar.enocassessment.utils.startNewActivity
import com.alisafdar.enocassessment.utils.visible


@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding

    @VisibleForTesting
    val viewModel :LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        binding.progressbar.visible(false)
        binding.loginButton.enable(false)

        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            binding.progressbar.visible(it is Resource.Loading)
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        viewModel.saveAccessTokens(
                            it.value.user.access_token.toString(), it.value.user.refresh_token.toString()
                        )
                        viewModel.insertUser(it.value.user)
                        requireActivity().startNewActivity(HomeActivity::class.java)
                    }
                }
                is Resource.Failure -> handleApiError(it) { login() }
                is Resource.Loading -> {}
            }
        })

        binding.passwordEditText.addTextChangedListener {
            val email = binding.emailEditText.text.toString().trim()
            binding.loginButton.enable(email.isNotEmpty() && it.toString().isNotEmpty())
        }

        binding.loginButton.setOnClickListener {
            login()
        }
    }

    private val authenticationObserver = Observer<LoginDataState> { dataState ->
        when (dataState) {
            is LoginDataState.Success -> {
                binding.progressbar.visibility = View.GONE
//                lifecycleScope.launch {
//                    val resource = dataState.body
//                    resource.
//                    viewModel.saveAccessTokens( it.value.user.access_token!!, it.value.user.refresh_token!!)
//                    requireActivity().startNewActivity(HomeActivity::class.java)
//                }
//                if (dataState.body?.headers() != null && dataState.body.headers().size > 0) {
//                    saveHeaders(dataState.body.headers())
//                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
//                    showItemsScreen()
//                }
            }
            is LoginDataState.Error -> {
                binding.progressbar.visibility = View.GONE
                Toast.makeText(context, dataState.message, Toast.LENGTH_SHORT).show()
            }
            is LoginDataState.ValidCredentialsState -> {
                binding.progressbar.visibility = View.VISIBLE
                resetEmailError()
                resetPasswordError()
            }
            is LoginDataState.InValidEmailState -> {
                setEmailError()

            }
            is LoginDataState.InValidPasswordState -> {
                resetEmailError()
                setPasswordError()
            }
        }
    }

    private fun setEmailError() {
        binding.emailEditText.error = "Please enter a valid email ID"
//        text_input_name.isErrorEnabled = true
    }

    private fun setPasswordError() {
        binding.passwordEditText.error = "Please enter a valid password"
//        binding.passwordEditText.isErrorEnabled = true
    }

    private fun resetEmailError() {
        binding.emailEditText.error = null
//        text_input_name.isErrorEnabled = false
    }

    private fun resetPasswordError() {
        binding.passwordEditText.error = null
//        text_input_password.isErrorEnabled = false
    }


    fun hideKeyboard(view: View) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun login() {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        viewModel.login(email, password)
    }
}