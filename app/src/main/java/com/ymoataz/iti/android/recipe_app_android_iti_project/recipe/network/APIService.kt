package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network

import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network.dto.Category
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network.dto.MyResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("api/json/v1/1/random.php")
    suspend fun getRandomMeal(): MyResponse

    @GET("api/json/v1/1/search.php")
    suspend fun search(@Query("s") mealName: String): MyResponse

    @GET("api/json/v1/1/search.php")
    suspend fun searchByFirstLetter(@Query("f") letter: String): MyResponse

    @GET("api/json/v1/1/categories.php")
    suspend fun getCategories(): Category

    @GET("api/json/v1/1/filter.php")
    suspend fun getMealsByCategory(@Query("c") category: String): MyResponse

    @GET("api/json/v1/1/lookup.php")
    suspend fun getMealByID(@Query("i") mealID: String): MyResponse

}