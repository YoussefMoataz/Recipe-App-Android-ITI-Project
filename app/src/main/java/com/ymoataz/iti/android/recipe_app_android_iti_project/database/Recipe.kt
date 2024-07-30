package com.ymoataz.iti.android.recipe_app_android_iti_project.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network.dto.Meal
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Recipe(
    @PrimaryKey(autoGenerate = true) val recipeId : Int,
    val userid: Int?,
    val meal: Meal?,
    var favourite: Boolean?
) : Parcelable
