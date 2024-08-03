package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.local.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.local.entities.Recipe
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network.models.meals.Meal

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: Recipe)

    @Query("select * from Recipe where userid = :userId")
    suspend fun getAllRecipes(userId: Int):List<Recipe>

    @Delete
    suspend fun deleteRecipe(recipe: Recipe)

    @Query("Delete FROM Recipe where meal =:meal")
    suspend fun deleteRecipeWithMeal(meal : Meal)

    @Query("SELECT count(*) FROM Recipe WHERE userid = :userId")
    fun getFavouriteRecipesCount(userId: Int): LiveData<Int>

}