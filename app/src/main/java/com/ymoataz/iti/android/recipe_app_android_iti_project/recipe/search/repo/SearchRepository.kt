package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.search.repo

import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network.dto.MyResponse

interface SearchRepository {
    suspend fun search(mealName:String):MyResponse
}