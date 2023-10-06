package com.example.mytasksactivity.view_model.tasks_view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mytasksactivity.repository.TasksUserRepository

/**

 * يتيح هذا المصنع إنشاء نموذج ViewModel مع تمرير تطبيق Android ومستودع اليوزر.
 */
class TasksUsersViewModelProviderFactory(
    val app: Application,
    private val tasksUserRepository: TasksUserRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TasksUserViewModel(app, tasksUserRepository) as T
    }
}