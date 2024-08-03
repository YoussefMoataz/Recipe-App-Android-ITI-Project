package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.search.repo

import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network.data_sources.RecipeRemoteDataSource

class SearchRepositoryImp(private val recipeRemoteDataSource: RecipeRemoteDataSource):
    SearchRepository {
    override suspend fun search(mealName : String) =recipeRemoteDataSource.search(mealName)

}