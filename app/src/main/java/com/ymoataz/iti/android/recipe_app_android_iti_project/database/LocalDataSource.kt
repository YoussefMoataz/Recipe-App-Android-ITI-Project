package com.ymoataz.iti.android.recipe_app_android_iti_project.database

import com.ymoataz.iti.android.recipe_app_android_iti_project.User

interface LocalDataSource {
    suspend fun insertUser(user: User)

    suspend fun login(email: String,password:String):Int

    suspend fun checkEmailIfExistBefore(email: String):Int

    suspend fun clearUsers()

}