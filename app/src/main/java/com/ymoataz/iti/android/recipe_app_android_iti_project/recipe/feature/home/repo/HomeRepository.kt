package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.home.repo

import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network.models.categories.Category
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network.models.meals.MyResponse

interface HomeRepository {
    suspend fun getRandomMeal(): MyResponse
    suspend fun searchByFirstLetter(letter: String): MyResponse
    suspend fun getCategories(): Category
    suspend fun getMealsByCategory(category: String): MyResponse
    suspend fun getMealByID(mealID: String): MyResponse
}