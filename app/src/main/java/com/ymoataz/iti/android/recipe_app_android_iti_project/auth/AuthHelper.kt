package com.ymoataz.iti.android.recipe_app_android_iti_project.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.transition.Visibility.MODE_IN
import androidx.transition.Visibility.Mode


object AuthHelper {
    private const val PREF_NAME="User_Pref"
    const val USER_ID="USER_ID"
    const val IS_LOGGED_IN="IS_LOGGED_IN"
    fun saveUserDataInSP(context: Context,userID:Int){
        val sharedPreferences=context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor=sharedPreferences.edit()
        editor.putInt(USER_ID,userID)
        editor.putBoolean(IS_LOGGED_IN,true)
        editor.commit()
    }
}