package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network

import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network.dto.MyResponse

interface RemoteDataSource {
    suspend fun getRandomMeal(): MyResponse
}