package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.home.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.home.repo.HomeRepository

class HomeViewModelFactory(private val repository: HomeRepository, val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(repository, context) as T
        }
        throw IllegalArgumentException("product view model ViewModel class")
    }
}