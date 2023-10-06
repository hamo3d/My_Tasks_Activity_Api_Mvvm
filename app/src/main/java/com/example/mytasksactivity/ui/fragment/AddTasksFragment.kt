package com.example.mytasksactivity.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mytasksactivity.databinding.FragmentAddTasksBinding
import com.example.mytasksactivity.model.TasksUser
import com.example.mytasksactivity.repository.TasksUserRepository
import com.example.mytasksactivity.utils.Resources
import com.example.mytasksactivity.view_model.tasks_view_model.TasksUserViewModel
import com.example.mytasksactivity.view_model.tasks_view_model.TasksUsersViewModelProviderFactory

class AddTasksFragment : Fragment() {
    private var _binding: FragmentAddTasksBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TasksUserViewModel
    private var editMode: String? = null
    private var tasksObject: TasksUser? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTasksBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val tasksUserRepository = TasksUserRepository()
        val viewModelProvider =
            TasksUsersViewModelProviderFactory(requireActivity().application, tasksUserRepository)
        viewModel = ViewModelProvider(this, viewModelProvider)[TasksUserViewModel::class.java]

        return root
    }

    override fun onStart() {
        super.onStart()

        val bundle = arguments
        if (bundle != null) {
            tasksObject = bundle.getParcelable("data_update")
            editMode = bundle.getString("mode")
        }

        if (editMode != null && editMode == "edit") {
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Edit Tasks"
            fillDataForEditing()
        } else {
            findNavController().currentDestination!!.label = "Add Tasks"
            setOnClickListener()
        }
    }

    private fun fillDataForEditing() {
        binding.titleEditText.setText(tasksObject!!.title)
        binding.addButton.text = "Update"

        binding.addButton.setOnClickListener {
            if (isValidSignDetails()) {
                updateTasks()
            }
        }
    }

    private fun updateTasks() {
        viewModel.updateTasks(tasksObject!!.id, binding.titleEditText.text.toString())
        viewModel.addTasks.observe(requireActivity(), Observer { response ->
            when (response) {
                is Resources.Success -> {
                    response.data?.let { result ->
                        findNavController().popBackStack()
                        showToast(result.message)
                    }
                }

                is Resources.Error -> {
                    response.message?.let { message ->
                        hideProgressBar()
                        showToast(message)
                    }
                }

                is Resources.loading -> {
                    showProgressBar()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
    }

    private fun setOnClickListener() {
        binding.addButton.setOnClickListener {
            if (isValidSignDetails()) {
                addTasks()
            }
        }
    }

    private fun addTasks() {
        viewModel.addTasksUser(binding.titleEditText.text.toString())
        viewModel.addTasks.observe(requireActivity()) { response ->
            when (response) {
                is Resources.Success -> {
                    response.data?.let { result ->
                        findNavController().popBackStack()
                        showToast(result.message)
                    }
                }

                is Resources.Error -> {
                    response.message?.let { message ->
                        hideProgressBar()
                        showToast(message)
                    }
                }

                is Resources.loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private var isLoading = false

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        binding.addButton.visibility = View.VISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        binding.addButton.visibility = View.INVISIBLE
        isLoading = true
    }

    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun isValidSignDetails(): Boolean {
        return if (binding.titleEditText.text.toString().trim().isEmpty()) {
            showToast("Enter Email")
            false
        } else true
    }
}
