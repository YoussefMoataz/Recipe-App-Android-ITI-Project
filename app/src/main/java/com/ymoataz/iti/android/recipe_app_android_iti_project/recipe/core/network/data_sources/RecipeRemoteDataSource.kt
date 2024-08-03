package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network.data_sources

import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network.models.categories.Category
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network.models.meals.MyResponse
import retrofit2.http.Query

interface RecipeRemoteDataSource {
    suspend fun getRandomMeal(): MyResponse
    suspend fun search(@Query("s") mealName: String): MyResponse
    suspend fun searchByFirstLetter(@Query("f") letter: String): MyResponse
    suspend fun getCategories(): Category
    suspend fun getMealsByCategory(@Query("c") category: String): MyResponse
    suspend fun getMealByID(mealID: String): MyResponse
}