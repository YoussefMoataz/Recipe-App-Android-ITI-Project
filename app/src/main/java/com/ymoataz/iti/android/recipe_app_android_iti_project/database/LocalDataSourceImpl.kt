package com.ymoataz.iti.android.recipe_app_android_iti_project.database

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

    override suspend fun checkEmailIfExistBefore(email: String): Int {
        return dao.checkEmailIfExistBefore(email)
    }

    override suspend fun clearUsers() {
         dao.clearUsers()
    }


}