package com.ymoataz.iti.android.recipe_app_android_iti_project.database

import androidx.lifecycle.LiveData
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network.dto.Meal

class RecipeLocalDataSourceImp(private val recipeDao: RecipeDao) : RecipeLocalDataSource {
    override suspend fun insertRecipe(recipe: Recipe)
    {
        recipeDao.insertRecipe(recipe)
    }

    override suspend fun getAllRecipes(userId: Int): List<Recipe>
    {
        return recipeDao.getAllRecipes(userId)
    }

    override suspend fun deleteRecipe(recipe: Recipe)
    {
        recipeDao.deleteRecipe(recipe)
    }

    override suspend fun deleteRecipeWithMeal(meal: Meal) {
       recipeDao.deleteRecipeWithMeal(meal)
    }

}