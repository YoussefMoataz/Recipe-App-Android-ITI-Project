package com.ymoataz.iti.android.recipe_app_android_iti_project.auth.core.local.data_sources

import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.core.local.entities.User

interface UserLocalDataSource {
    suspend fun insertUser(user: User)

    suspend fun login(email: String,password:String):Int

    suspend fun checkEmailIfExistBefore(email: String):Int

    suspend fun clearUsers()

    suspend fun getUserIdByEmail(email: String):Int

}