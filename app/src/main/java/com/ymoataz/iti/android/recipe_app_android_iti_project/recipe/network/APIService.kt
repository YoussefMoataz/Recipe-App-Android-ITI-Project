package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network

import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network.dto.MyResponse
import retrofit2.http.GET

interface APIService {
    @GET("api/json/v1/1/random.php")
    suspend fun getRandomMeal(): MyResponse
}