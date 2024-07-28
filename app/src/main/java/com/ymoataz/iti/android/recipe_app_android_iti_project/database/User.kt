package com.ymoataz.iti.android.recipe_app_android_iti_project.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey (autoGenerate = true)  val id:Int=0,
    val fName:String,
    val lName:String,
    val email:String,
    val password:String
)
