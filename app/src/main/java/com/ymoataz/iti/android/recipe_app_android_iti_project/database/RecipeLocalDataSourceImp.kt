package com.ymoataz.iti.android.recipe_app_android_iti_project.database

import androidx.lifecycle.LiveData

class RecipeLocalDataSourceImp(private val recipeDao: RecipeDao) : RecipeLocalDataSource {
    override suspend fun insertRecipe(recipe: Recipe)
    {
        recipeDao.insertRecipe(recipe)
    }

    override fun getAllRecipes(userId: Int): LiveData<List<Recipe>>
    {
        return recipeDao.getAllRecipes(userId)
    }

    override suspend fun deleteRecipe(recipe: Recipe)
    {
        recipeDao.deleteRecipe(recipe)
    }

}