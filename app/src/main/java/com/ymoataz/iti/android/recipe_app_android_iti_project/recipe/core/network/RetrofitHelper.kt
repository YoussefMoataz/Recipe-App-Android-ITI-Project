package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network

import com.google.gson.GsonBuilder
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network.services.APIService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private const val BASE_URL = "https://www.themealdb.com/"
    private val gson = GsonBuilder().serializeNulls().create()
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    val apiService: APIService by lazy {
        retrofit.create(APIService::class.java)
    }
}