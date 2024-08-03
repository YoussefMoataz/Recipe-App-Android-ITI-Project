package com.ymoataz.iti.android.recipe_app_android_iti_project.auth.core.common

import android.content.Context


object AuthHelper {
    private const val PREF_NAME = "User_Pref"
    const val USER_ID = "USER_ID"
    const val IS_LOGGED_IN = "IS_LOGGED_IN"
    fun saveUserDataInSP(context: Context, userID: Int) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(USER_ID, userID)
        editor.putBoolean(IS_LOGGED_IN, true)
        editor.commit()
    }

    fun getUserID(context: Context): Int? {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.getInt(USER_ID, -1).also {
            if (it == -1) {
                return null
            }
            return it
        }
    }

    fun isUserLoggedIn(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false)
    }

    fun logout(context: Context){
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(IS_LOGGED_IN, false)
        editor.commit()
    }
}