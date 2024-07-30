package com.ymoataz.iti.android.recipe_app_android_iti_project.auth.repo

import com.ymoataz.iti.android.recipe_app_android_iti_project.database.User
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.LocalDataSource

class UserRepoImpl(private val localDataSource: LocalDataSource): UserRepo {

    override suspend fun insertUser(user: User) {
        localDataSource.insertUser(user)
    }

    override suspend fun login(email: String, password: String): Boolean {
        return localDataSource.login(email, password)>0
    }

    override suspend fun checkEmailIfExistBefore(email: String): Boolean {
        return localDataSource.checkEmailIfExistBefore(email)>0
    }

    override suspend fun clearUsers() {
        localDataSource.clearUsers()
    }

    override suspend fun getUserIdByEmail(email: String): Int {
        return localDataSource.getUserIdByEmail(email)
    }
}