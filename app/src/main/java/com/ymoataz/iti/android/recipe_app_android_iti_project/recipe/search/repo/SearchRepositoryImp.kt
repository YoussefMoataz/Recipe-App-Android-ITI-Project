package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.search.repo

import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network.RemoteDataSource
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network.dto.MyResponse

class SearchRepositoryImp(private val remoteDataSource: RemoteDataSource):SearchRepository {
    override suspend fun search(mealName : String) =remoteDataSource.search(mealName)

}