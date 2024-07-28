package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.adapter

import android.os.Parcelable
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network.dto.Meal
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(val userid: Int?, val meal: Meal?, var favourite: Boolean?) : Parcelable
