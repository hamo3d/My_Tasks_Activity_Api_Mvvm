package com.example.mytasksactivity.adapter

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mytasksactivity.databinding.ItemTaksPreviewBinding
import com.example.mytasksactivity.model.TasksUser

class TasksAdapter : RecyclerView.Adapter<TasksAdapter.TasksViewHolder>() {
    inner class TasksViewHolder(private var binding: ItemTaksPreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(tasksUser: TasksUser) {
            binding.titleTextView.text = tasksUser.title
            binding.createAtTextView.text =
                tasksUser.created_at?.let { convertDateTimeToFormattedDateString(it) }
            binding.updatedAtTextView.text =
                tasksUser.updated_at?.let { convertDateTimeToFormattedDateString(it) }
            binding.container.setOnClickListener { onItemClickListener?.let { it(tasksUser) } }
        }

        @SuppressLint("SimpleDateFormat")
        fun convertDateTimeToFormattedDateString(dateTimeString: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
            val outputFormat = SimpleDateFormat("yyyy-MM-dd")
            val date = inputFormat.parse(dateTimeString)
            return outputFormat.format(date)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val binding =
            ItemTaksPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TasksViewHolder(binding)
    }

    private val differCallback = object : DiffUtil.ItemCallback<TasksUser>() {
        override fun areItemsTheSame(oldItem: TasksUser, newItem: TasksUser): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TasksUser, newItem: TasksUser): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        holder.setData(differ.currentList[position])
    }

    private var onItemClickListener: ((TasksUser) -> Unit)? = null

    fun setOnItemClickListener(listener: (TasksUser) -> Unit) {
        onItemClickListener = listener
    }

}