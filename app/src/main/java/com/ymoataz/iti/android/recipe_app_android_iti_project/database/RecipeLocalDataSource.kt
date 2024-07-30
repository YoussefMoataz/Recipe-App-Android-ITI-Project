package com.ymoataz.iti.android.recipe_app_android_iti_project.database

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

interface RecipeLocalDataSource {
    suspend fun insertRecipe(recipe: Recipe)

    fun getAllRecipes(userId: Int): LiveData<List<Recipe>>

    suspend fun deleteRecipe(recipe: Recipe)
}