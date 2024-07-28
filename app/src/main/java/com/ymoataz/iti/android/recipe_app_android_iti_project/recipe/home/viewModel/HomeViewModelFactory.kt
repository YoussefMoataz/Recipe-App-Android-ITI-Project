package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.home.repo.HomeRepository

class HomeViewModelFactory(private val repository: HomeRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("product view model ViewModel class")
    }
}