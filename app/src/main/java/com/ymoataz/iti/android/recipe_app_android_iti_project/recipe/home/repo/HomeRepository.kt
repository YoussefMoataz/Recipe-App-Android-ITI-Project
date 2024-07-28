package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.home.repo

import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network.dto.MyResponse

interface HomeRepository {
    suspend fun getRandomMeal(): MyResponse
}