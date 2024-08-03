package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.search.repo

import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network.models.meals.MyResponse

interface SearchRepository {
    suspend fun search(mealName:String): MyResponse
}