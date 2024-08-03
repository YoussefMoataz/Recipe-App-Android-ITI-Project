package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.favourite.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.local.entities.Recipe
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.favourite.repo.FavouriteRepo
import kotlinx.coroutines.launch

class FavouriteViewModel (private val favouriteRepo: FavouriteRepo): ViewModel(){
    private val _favouriteRecipes = MutableLiveData<List<Recipe>>()
    val favouriteRecipes : LiveData<List<Recipe>> = _favouriteRecipes

     fun insertRecipe(recipe: Recipe)
     {
         viewModelScope.launch {
             favouriteRepo.insertRecipe(recipe)
             recipe.userid?.let {
                 getAllRecipes(it)
             }
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
             recipe.userid?.let {
                 getAllRecipes(it)
             }
         }
     }


}