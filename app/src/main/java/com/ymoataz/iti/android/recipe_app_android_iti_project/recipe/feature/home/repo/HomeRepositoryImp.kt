package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.home.repo

import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network.data_sources.RecipeRemoteDataSource
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network.models.categories.Category
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network.models.meals.MyResponse

class HomeRepositoryImp(private val recipeRemoteDataSource: RecipeRemoteDataSource):
    HomeRepository {
    override suspend fun getRandomMeal(): MyResponse {
        return recipeRemoteDataSource.getRandomMeal()
    }

    override suspend fun searchByFirstLetter(letter: String): MyResponse {
        return recipeRemoteDataSource.searchByFirstLetter(letter)
    }

    override suspend fun getCategories(): Category {
        return recipeRemoteDataSource.getCategories()
    }

    override suspend fun getMealsByCategory(category: String): MyResponse {
        return recipeRemoteDataSource.getMealsByCategory(category)
    }

    override suspend fun getMealByID(mealID: String): MyResponse {
        return recipeRemoteDataSource.getMealByID(mealID)
    }


}