package com.ymoataz.iti.android.recipe_app_android_iti_project.auth.repo

import com.ymoataz.iti.android.recipe_app_android_iti_project.database.User

interface UserRepo {
    suspend fun insertUser(user: User)
    suspend fun login(email: String,password:String):Boolean
    suspend fun checkEmailIfExistBefore(email: String):Boolean
    suspend fun clearUsers()
}