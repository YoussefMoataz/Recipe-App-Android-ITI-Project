package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.home.repo

import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network.dto.Category
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network.dto.MyResponse

interface HomeRepository {
    suspend fun getRandomMeal(): MyResponse
    suspend fun searchByFirstLetter(letter: String): MyResponse
    suspend fun getCategories(): Category
    suspend fun getMealsByCategory(category: String): MyResponse
}