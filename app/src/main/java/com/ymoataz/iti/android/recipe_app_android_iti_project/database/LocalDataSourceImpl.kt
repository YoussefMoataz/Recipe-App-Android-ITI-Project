package com.ymoataz.iti.android.recipe_app_android_iti_project.database

import com.ymoataz.iti.android.recipe_app_android_iti_project.User

class LocalDataSourceImpl(private val dao: UserDao): LocalDataSource {
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

}