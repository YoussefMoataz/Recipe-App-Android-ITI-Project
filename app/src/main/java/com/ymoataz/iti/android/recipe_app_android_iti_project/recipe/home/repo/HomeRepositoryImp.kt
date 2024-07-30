package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.home.repo

import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network.RemoteDataSource
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network.dto.MyResponse

class HomeRepositoryImp(private val remoteDataSource: RemoteDataSource): HomeRepository {
    override suspend fun getRandomMeal(): MyResponse {
        return remoteDataSource.getRandomMeal()
    }

    override suspend fun searchByFirstLetter(letter: String): MyResponse {
        return remoteDataSource.searchByFirstLetter(letter)
    }
}