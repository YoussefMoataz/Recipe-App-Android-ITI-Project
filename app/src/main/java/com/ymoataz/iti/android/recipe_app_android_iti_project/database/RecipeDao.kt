package com.ymoataz.iti.android.recipe_app_android_iti_project.database

import android.adservices.adid.AdId
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: Recipe)

    @Query("select * from Recipe where userid = :userId")
    fun getAllRecipes(userId: Int):LiveData<List<Recipe>>

    @Delete
    suspend fun deleteRecipe(recipe: Recipe)

}