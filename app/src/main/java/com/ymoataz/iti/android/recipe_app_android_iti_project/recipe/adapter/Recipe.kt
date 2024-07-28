package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.adapter

import com.ymoataz.iti.android.recipe_app_android_iti_project.database.User
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network.dto.Meal

data class Recipe(val userid : Int, val meal : Meal, var favourite : Boolean)
