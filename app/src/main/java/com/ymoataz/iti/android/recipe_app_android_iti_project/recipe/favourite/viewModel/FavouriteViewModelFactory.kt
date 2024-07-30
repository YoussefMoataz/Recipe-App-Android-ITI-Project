package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.favourite.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.favourite.repo.FavouriteRepo
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.search.repo.SearchRepository
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.search.viewModel.SearchViewModel

class FavouriteViewModelFactory (private val favouriteRepo: FavouriteRepo):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(FavouriteViewModel::class.java))
        {
            FavouriteViewModel(favouriteRepo) as T
        }
        else
        {
            throw IllegalArgumentException("")
        }
    }
}
