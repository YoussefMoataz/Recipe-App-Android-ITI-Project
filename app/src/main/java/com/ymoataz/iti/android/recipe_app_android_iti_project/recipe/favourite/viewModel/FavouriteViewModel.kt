package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.favourite.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.Recipe
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.favourite.repo.FavouriteRepo
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.search.repo.SearchRepository
import kotlinx.coroutines.launch

class FavouriteViewModel (private val favouriteRepo: FavouriteRepo): ViewModel(){
    private val _favouriteRecipes = MutableLiveData<List<Recipe>>()
    val favouriteRecipes : LiveData<List<Recipe>> = _favouriteRecipes

     fun insertRecipe(recipe: Recipe)
     {
         viewModelScope.launch {
             favouriteRepo.insertRecipe(recipe)
         }
     }

    fun getAllRecipes(userId: Int)
    {
        viewModelScope.launch {
            _favouriteRecipes.postValue(favouriteRepo.getAllRecipes(userId))
        }
    }

     fun deleteRecipe(recipe: Recipe)
     {
         viewModelScope.launch {
             favouriteRepo.deleteRecipe(recipe)
         }
     }


}