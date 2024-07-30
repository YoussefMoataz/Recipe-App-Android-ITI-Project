package com.ymoataz.iti.android.recipe_app_android_iti_project.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("select count(*) from User where email =:email and password=:password")
    suspend fun login(email: String,password:String):Int

    @Query("select count(*) from User where email =:email ")
    suspend fun checkEmailIfExistBefore(email: String):Int

    @Query("delete from User")
    suspend fun clearUsers()

    @Query("select id from User where email =:email")
    suspend fun getUserIdByEmail(email: String):Int
}