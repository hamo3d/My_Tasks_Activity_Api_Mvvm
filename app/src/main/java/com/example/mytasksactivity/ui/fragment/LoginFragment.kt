package com.example.mytasksactivity.ui.fragment

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mytasksactivity.R
import com.example.mytasksactivity.databinding.FragmentLoginBinding
import com.example.mytasksactivity.repository.UserRepository
import com.example.mytasksactivity.utils.Resources
import com.example.mytasksactivity.view_model.login_view_model.UserViewModel
import com.example.mytasksactivity.view_model.login_view_model.UsersViewModelProviderFactory


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onStart() {
        super.onStart()
        val userRepository = UserRepository()
        val viewModelProvider =
            UsersViewModelProviderFactory(requireActivity().application, userRepository)
        viewModel = ViewModelProvider(this, viewModelProvider)[UserViewModel::class.java]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        setOnCLickLitener()
    }

    private fun setOnCLickLitener() {
        binding.loginButton.setOnClickListener {
            if (isValidSignDetails()!!) {
                login()
            }
        }
        binding.risgterTextView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun login() {
        viewModel.login(
            binding.emailEditText.text.toString(), binding.passwordEditText.text.toString()
        )
        viewModel.users.observe(requireActivity(), Observer { response ->
            when (response) {
                is Resources.Success -> {
                    response.data?.let {
                        val currentDestinationId = findNavController().currentDestination?.id
                        if (currentDestinationId == R.id.navigation_login) {
                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                            val navController = findNavController()
                            val navGraph =
                                navController.navInflater.inflate(R.navigation.mobile_navigation)
                            navGraph.setStartDestination(R.id.navigation_home)
                            navController.graph = navGraph
                        } else {
                        }
                        hideProgressPar()

                    }
                }


                is Resources.Error -> {
                    response.message?.let { message ->
                        hideProgressPar()
                        Toast.makeText(
                            activity, message, Toast.LENGTH_LONG
                        ).show()
                    }
                }



                is Resources.loading -> {
                    showProgressPar()

                }
            }

        })
    }


    private var isLoading = false

    private fun hideProgressPar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        binding.loginButton.visibility = View.VISIBLE
        isLoading = false

    }

    private fun showProgressPar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        binding.loginButton.visibility = View.INVISIBLE
        isLoading = true

    }

        private fun showToast(message: String) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }

    private fun isValidSignDetails(): Boolean? {
        return if (binding.emailEditText.text.toString().trim().isEmpty()) {
            showToast("Enter email")
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailEditText.text.toString())
                .matches()
        ) {
            showToast("Must valid email")
            false
        } else if (binding.passwordEditText.text.toString().isEmpty()) {
            showToast("Enter password")
            false
        } else true
    }


}