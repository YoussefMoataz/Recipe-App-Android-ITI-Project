package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.favourite.repo

import androidx.lifecycle.LiveData
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.local.entities.Recipe

interface FavouriteRepo {
    suspend fun insertRecipe(recipe: Recipe)

    suspend fun getAllRecipes(userId: Int): List<Recipe>

    suspend fun deleteRecipe(recipe: Recipe)

    fun getFavouriteRecipesCount(userId: Int): LiveData<Int>
}