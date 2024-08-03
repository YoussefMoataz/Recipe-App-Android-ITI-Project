package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.local.data_sources

import androidx.lifecycle.LiveData
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.local.entities.Recipe
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network.models.meals.Meal

interface RecipeLocalDataSource {
    suspend fun insertRecipe(recipe: Recipe)

    suspend fun getAllRecipes(userId: Int): List<Recipe>

    suspend fun deleteRecipe(recipe: Recipe)

    suspend fun deleteRecipeWithMeal(meal : Meal)

    fun getFavouriteRecipesCount(userId: Int): LiveData<Int>
}