package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.search.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.search.repo.SearchRepository

class SearchViewModelFactory (private val searchRepository: SearchRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(SearchViewModel::class.java))
        {
            SearchViewModel(searchRepository) as T
        }
        else
        {
            throw IllegalArgumentException("")
        }
    }
}
