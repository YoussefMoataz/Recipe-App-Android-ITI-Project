package com.ymoataz.iti.android.recipe_app_android_iti_project.database

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network.dto.Meal

interface RecipeLocalDataSource {
    suspend fun insertRecipe(recipe: Recipe)

    suspend fun getAllRecipes(userId: Int): List<Recipe>

    suspend fun deleteRecipe(recipe: Recipe)

    suspend fun deleteRecipeWithMeal(meal : Meal)
}