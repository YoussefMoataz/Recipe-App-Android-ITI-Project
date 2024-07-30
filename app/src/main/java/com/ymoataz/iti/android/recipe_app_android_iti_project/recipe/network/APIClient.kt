package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network

import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network.dto.MyResponse

object APIClient:RemoteDataSource {
    override suspend fun getRandomMeal(): MyResponse {
        return RetrofitHelper.apiService.getRandomMeal()
    }

    override suspend fun search(mealName: String): MyResponse {
        return RetrofitHelper.apiService.search(mealName)
    }

    override suspend fun searchByFirstLetter(letter: String): MyResponse {
        return RetrofitHelper.apiService.searchByFirstLetter(letter)
    }

}