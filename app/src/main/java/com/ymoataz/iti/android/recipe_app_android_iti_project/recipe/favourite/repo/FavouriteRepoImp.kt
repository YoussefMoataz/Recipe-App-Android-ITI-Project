package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.favourite.repo

import androidx.lifecycle.LiveData
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.Recipe
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.RecipeLocalDataSource

class FavouriteRepoImp(private val recipeLocalDataSource: RecipeLocalDataSource) : FavouriteRepo{
    override suspend fun insertRecipe(recipe: Recipe) {
        recipeLocalDataSource.insertRecipe(recipe)
    }

    override suspend fun getAllRecipes(userId: Int): List<Recipe> {
       return recipeLocalDataSource.getAllRecipes(userId)
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        recipeLocalDataSource.deleteRecipe(recipe)
    }

}