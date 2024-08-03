package com.ymoataz.iti.android.recipe_app_android_iti_project.auth.core.local.repo

import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.core.local.entities.User
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.core.local.data_sources.UserLocalDataSource

class UserRepoImpl(private val userLocalDataSource: UserLocalDataSource): UserRepo {

    override suspend fun insertUser(user: User) {
        userLocalDataSource.insertUser(user)
    }

    override suspend fun login(email: String, password: String): Boolean {
        return userLocalDataSource.login(email, password)>0
    }

    override suspend fun checkEmailIfExistBefore(email: String): Boolean {
        return userLocalDataSource.checkEmailIfExistBefore(email)>0
    }

    override suspend fun clearUsers() {
        userLocalDataSource.clearUsers()
    }

    override suspend fun getUserIdByEmail(email: String): Int {
        return userLocalDataSource.getUserIdByEmail(email)
    }
}