package com.example.mytasksactivity.view_model.login_view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mytasksactivity.repository.UserRepository

/**

 * يتيح هذا المصنع إنشاء نموذج ViewModel مع تمرير تطبيق Android ومستودع اليوزر.
 */
class UsersViewModelProviderFactory(
    val app: Application,
    private val newsRepository: UserRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserViewModel(app, newsRepository) as T
    }
}