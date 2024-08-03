package com.ymoataz.iti.android.recipe_app_android_iti_project.auth.core.local.data_sources.impl

import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.core.local.data_sources.UserLocalDataSource
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.core.local.entities.User
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.core.local.daos.UserDao

class UserLocalDataSourceImpl(private val dao: UserDao): UserLocalDataSource {
    //private var dao:UserDao

   /* init {
        val db=AppDatabase.getDatabase(context)
        dao=db.userDao()
    }*/

    override suspend fun insertUser(user: User) {
       dao.insertUser(user)
    }

    override suspend fun login(email: String, password: String): Int {
        return dao.login(email, password)
    }

    override suspend fun checkEmailIfExistBefore(email: String): Int {
        return dao.checkEmailIfExistBefore(email)
    }

    override suspend fun clearUsers() {
         dao.clearUsers()
    }

    override suspend fun getUserIdByEmail(email: String): Int {
        return dao.getUserIdByEmail(email)
    }
}