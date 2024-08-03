package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.local.data_sources.impl

import androidx.lifecycle.LiveData
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.local.daos.RecipeDao
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.local.entities.Recipe
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.local.data_sources.RecipeLocalDataSource
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network.models.meals.Meal

class RecipeLocalDataSourceImpl(private val recipeDao: RecipeDao) : RecipeLocalDataSource {
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

    override fun getFavouriteRecipesCount(userId: Int): LiveData<Int> {
        return recipeDao.getFavouriteRecipesCount(userId)
    }

}