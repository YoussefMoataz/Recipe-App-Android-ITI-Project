package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network

import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network.dto.MyResponse
import retrofit2.http.Query

interface RemoteDataSource {
    suspend fun getRandomMeal(): MyResponse
    suspend fun search(@Query("s") mealName: String): MyResponse

}