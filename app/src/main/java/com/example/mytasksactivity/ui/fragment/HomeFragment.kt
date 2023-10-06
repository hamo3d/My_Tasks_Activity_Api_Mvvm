package com.example.mytasksactivity.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytasksactivity.R
import com.example.mytasksactivity.adapter.TasksAdapter
import com.example.mytasksactivity.databinding.FragmentHomeBinding
import com.example.mytasksactivity.repository.TasksUserRepository
import com.example.mytasksactivity.utils.Resources
import com.example.mytasksactivity.view_model.tasks_view_model.TasksUserViewModel
import com.example.mytasksactivity.view_model.tasks_view_model.TasksUsersViewModelProviderFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    val binding: FragmentHomeBinding
        get() = _binding ?: throw IllegalStateException("Binding is not available.")

    lateinit var viewModel: TasksUserViewModel
    lateinit var tasksAdapter: TasksAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tasksUserRepository = TasksUserRepository()
        val tasksUsersViewModelProviderFactory =
            TasksUsersViewModelProviderFactory(requireActivity().application, tasksUserRepository)
        viewModel = ViewModelProvider(
            this,
            tasksUsersViewModelProviderFactory
        )[TasksUserViewModel::class.java]
        setUpRecycler()
        viewModel.getTasksUser()
        viewModel.tasks.observe(requireActivity(), Observer { response ->
            when (response) {
                is Resources.Success -> {
                    response.data?.let { result ->
                        hideProgressPar()
                        tasksAdapter.differ.submitList(result.data)

                    }
                }


                is Resources.Error -> {
                    response.message?.let { message ->
                        hideProgressPar()
                        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
                    }
                }

                is Resources.loading -> {
                    showProgressPar()
                }
            }

        })


        var itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val id = tasksAdapter.differ.currentList[position].id

                MaterialAlertDialogBuilder(requireActivity()).setTitle("هل تريد حذف العنصر ")
                    .setMessage("سيتم حذف العنصر بشكل نهائي ")
                    .setNegativeButton("لا") { _, _ ->
                        tasksAdapter.notifyDataSetChanged()
                    }.setPositiveButton("نعم") { _, _ ->
                        val id = tasksAdapter.differ.currentList[position].id
                        viewModel.deleteTasksUser(id)
                        viewModel.getTasksUser()
                        Snackbar.make(requireView(), "deleted tasks success", Snackbar.LENGTH_LONG)
                            .show()


                    }.show()

            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rcTasksUsers)
        }

        viewModel.delete.observe(requireActivity(), Observer { response ->
            when (response) {
                is Resources.Success -> {
                    response.data?.let { result ->


                    }
                }


                is Resources.Error -> {
                    response.message?.let { message ->
                        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
                    }
                }

                is Resources.loading -> {
                    showProgressPar()
                }
            }

        })
    }

    override fun onResume() {
        super.onResume()
        setOnClickListener()
    }


    private fun setOnClickListener() {
        binding.addFAB.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_addTasksFragment)
        }


        tasksAdapter.setOnItemClickListener {
            val addFragment = HomeFragment()
            val bundle = Bundle().apply {
                putParcelable("data_update", it)
             putString("mode", "edit")
            }
            addFragment.arguments = bundle

            findNavController().navigate(
                R.id.action_navigation_home_to_addTasksFragment,
                bundle
            )
        }
    }


    private fun setUpRecycler() {
        tasksAdapter = TasksAdapter()
        binding.rcTasksUsers.apply {
            adapter = tasksAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    var isLoading = false
    private fun hideProgressPar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false

    }

    private fun showProgressPar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}









