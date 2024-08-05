package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.search.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.search.repo.SearchRepository

class SearchViewModelFactory (private val searchRepository: SearchRepository ,val context: Context):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(SearchViewModel::class.java))
        {
            SearchViewModel(searchRepository, context) as T
        }
        else
        {
            throw IllegalArgumentException("")
        }
    }
}
