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
import com.example.mytasksactivity.databinding.FragmentRegisterBinding
import com.example.mytasksactivity.repository.UserRepository
import com.example.mytasksactivity.utils.Resources
import com.example.mytasksactivity.view_model.login_view_model.UserViewModel
import com.example.mytasksactivity.view_model.login_view_model.UsersViewModelProviderFactory


class RegisterFragment : Fragment() {


    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val userRepository = UserRepository()
        val userViewModelProvider =
            UsersViewModelProviderFactory(requireActivity().application, userRepository)
        viewModel = ViewModelProvider(this, userViewModelProvider)[UserViewModel::class.java]
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
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
                register()
            }
        }
    }

    private fun register() {

        viewModel.register(binding.fullNameEditText.text.toString(),binding.emailEditText.text.toString(),binding.passwordEditText.text.toString(),getGender())
        viewModel.users.observe(requireActivity(), Observer {response ->
            when (response) {
                is Resources.Success -> {
                    response.data?.let {
                        findNavController().popBackStack()
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

    private fun getGender(): String {
        val selectedRadioButtonId = binding.rd.checkedRadioButtonId
        val result: String = when (selectedRadioButtonId) {
            R.id.radioButton -> "M" // إذا تم اختيار ذكر
            R.id.radioButton2 -> "M" // إذا تم اختيار أنثى
            else -> "" // لم يتم اختيار شيء
        }

        return result
    }


    override fun onDestroy() {
        super.onDestroy()

    }

}