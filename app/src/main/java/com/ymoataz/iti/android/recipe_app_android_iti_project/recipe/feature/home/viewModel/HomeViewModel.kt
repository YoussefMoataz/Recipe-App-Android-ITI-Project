package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.home.repo.HomeRepository
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network.models.categories.Category
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network.models.meals.Meal
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network.models.meals.MyResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {

    private val _randomMeal = MutableLiveData<MyResponse>()
    val randomMeal: LiveData<MyResponse> = _randomMeal

    private val _searchedMeal = MutableLiveData<MyResponse>()
    val searchedMeal: LiveData<MyResponse> = _searchedMeal

    private val _categories = MutableLiveData<Category>()
    val categories: LiveData<Category> = _categories

    private val _mealsByCategory = MutableLiveData<MyResponse>()
    val mealsByCategory: LiveData<MyResponse> = _mealsByCategory

    fun getRandomMeal() {
        viewModelScope.launch {
            val response = homeRepository.getRandomMeal()
            _randomMeal.value = response
        }
    }

    fun searchByFirstLetter(letter: String) {
        viewModelScope.launch {
            val response = homeRepository.searchByFirstLetter(letter)
            _searchedMeal.value = response
        }
    }

    fun getCategories() {
        viewModelScope.launch {
            val response = homeRepository.getCategories()
            _categories.value = response
        }
    }

    fun getMealsByCategory(category: String) {
        viewModelScope.launch {
            val response = homeRepository.getMealsByCategory(category)
//            _mealsByCategory.value = response
            val mealDeferreds = response.meals.map { meal ->
                async { getMealByID(meal.idMeal!!) }
            }

            val meals = mealDeferreds.awaitAll()
            _searchedMeal.value = MyResponse(meals)
        }
    }

    suspend fun getMealByID(mealID: String): Meal {
        return homeRepository.getMealByID(mealID).meals[0]
    }
}