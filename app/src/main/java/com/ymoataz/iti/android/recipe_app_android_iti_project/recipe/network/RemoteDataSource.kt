package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network

import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network.dto.Category
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network.dto.MyResponse
import retrofit2.http.Query

interface RemoteDataSource {
    suspend fun getRandomMeal(): MyResponse
    suspend fun search(@Query("s") mealName: String): MyResponse
    suspend fun searchByFirstLetter(@Query("f") letter: String): MyResponse
    suspend fun getCategories(): Category
    suspend fun getMealsByCategory(@Query("c") category: String): MyResponse
}