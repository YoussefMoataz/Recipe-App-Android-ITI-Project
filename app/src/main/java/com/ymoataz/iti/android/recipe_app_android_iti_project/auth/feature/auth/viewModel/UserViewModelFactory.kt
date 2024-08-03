package com.ymoataz.iti.android.recipe_app_android_iti_project.auth.feature.auth.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.core.repo.UserRepo

class UserViewModelFactory(private val repository: UserRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)){
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class!!")
    }
}
