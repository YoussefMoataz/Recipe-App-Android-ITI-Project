package com.ymoataz.iti.android.recipe_app_android_iti_project.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.core.local.daos.UserDao
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.core.local.entities.User
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.local.entities.Recipe
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.local.converters.MealConverter
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.local.daos.RecipeDao

@Database(entities = [User::class, Recipe::class], version = 1)
@TypeConverters(MealConverter::class)
abstract class AppDatabase:RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "user_database"
                ).fallbackToDestructiveMigration().build().also { INSTANCE =it }

            }
        }
    }
}