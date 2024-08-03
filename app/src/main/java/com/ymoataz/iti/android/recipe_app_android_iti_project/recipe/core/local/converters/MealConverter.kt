package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network.models.meals.Meal

class MealConverter {

    @TypeConverter
    fun fromMealModel(meal: Meal): String = Gson().toJson(meal)

    @TypeConverter
    fun fromMealString(meal: String): Meal = Gson().fromJson(meal, Meal::class.java)

}