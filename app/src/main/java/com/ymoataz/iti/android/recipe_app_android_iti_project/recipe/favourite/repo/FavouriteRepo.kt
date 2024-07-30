package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.favourite.repo

import androidx.lifecycle.LiveData
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.Recipe

interface FavouriteRepo {
    suspend fun insertRecipe(recipe: Recipe)

    fun getAllRecipes(userId: Int): LiveData<List<Recipe>>

    suspend fun deleteRecipe(recipe: Recipe)
}