package com.ymoataz.iti.android.recipe_app_android_iti_project.repo

import com.ymoataz.iti.android.recipe_app_android_iti_project.User
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.LocalDataSource

class UserRepoImpl(private val localDataSource: LocalDataSource): UserRepo {

    override suspend fun insertUser(user: User) {
        localDataSource.insertUser(user)
    }

    override suspend fun login(email: String, password: String): Boolean {
        return localDataSource.login(email, password)>0
    }
}