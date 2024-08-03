package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network.data_sources.impl

import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network.RetrofitHelper
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network.data_sources.RecipeRemoteDataSource
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network.models.categories.Category
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network.models.meals.MyResponse

object RecipeRemoteDataSourceImpl: RecipeRemoteDataSource {
    override suspend fun getRandomMeal(): MyResponse {
        return RetrofitHelper.apiService.getRandomMeal()
    }

    override suspend fun search(mealName: String): MyResponse {
        return RetrofitHelper.apiService.search(mealName)
    }

    override suspend fun searchByFirstLetter(letter: String): MyResponse {
        return RetrofitHelper.apiService.searchByFirstLetter(letter)
    }

    override suspend fun getCategories(): Category {
        return RetrofitHelper.apiService.getCategories()
    }

    override suspend fun getMealsByCategory(category: String): MyResponse {
        return RetrofitHelper.apiService.getMealsByCategory(category)
    }

    override suspend fun getMealByID(mealID: String): MyResponse {
        return RetrofitHelper.apiService.getMealByID(mealID)
    }


}