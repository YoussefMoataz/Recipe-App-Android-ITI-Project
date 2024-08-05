package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.home.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.core.common.AuthHelper
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.AppDatabase
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.common.connectivity.ConnectivityObserver
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.common.connectivity.NetworkConnectivityObserver
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.common.helpers.ConnectivityHelper
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network.models.categories.Category
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network.models.meals.Meal
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network.models.meals.MyResponse
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.home.repo.HomeRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class HomeViewModel(private val homeRepository: HomeRepository, val context: Context) :
    ViewModel() {

    private val _randomMeal = MutableLiveData<MyResponse>()
    val randomMeal: LiveData<MyResponse> = _randomMeal

    private val _searchedMeal = MutableLiveData<MyResponse>()
    val searchedMeal: LiveData<MyResponse> = _searchedMeal

    private val _categories = MutableLiveData<Category>()
    val categories: LiveData<Category> = _categories

    private val _mealsByCategory = MutableLiveData<MyResponse>()
    val mealsByCategory: LiveData<MyResponse> = _mealsByCategory

    private val _connectionStatus = MutableLiveData<ConnectivityObserver.Status>()
    val connectionStatus : LiveData<ConnectivityObserver.Status> = _connectionStatus

    init{
        val connectivityObserver = NetworkConnectivityObserver(context)

        viewModelScope.launch {
            connectivityObserver.observe().collect { status ->
                _connectionStatus.value = status

                getRandomMeal(status)
                searchByFirstLetter(getRandomLetter().toString(), status)
                getCategories(status)
            }
        }
    }

    private fun getRandomMeal(connectivityStatus: ConnectivityObserver.Status) {
        viewModelScope.launch {
            if (connectivityStatus == ConnectivityObserver.Status.Available) {
                val response = homeRepository.getRandomMeal()
                _randomMeal.value = response
            } else {
                AuthHelper.getUserID(context)?.let { userId ->
                    val response = AppDatabase.getDatabase(context).recipeDao().getAllRecipes(userId)
                    _randomMeal.value = MyResponse(response.map { it.meal!! })
                }

            }
        }
    }

    private fun searchByFirstLetter(letter: String, connectivityStatus: ConnectivityObserver.Status) {
        viewModelScope.launch {
            if (connectivityStatus == ConnectivityObserver.Status.Available) {
                var response = homeRepository.searchByFirstLetter(letter)
                while (response?.meals.isNullOrEmpty()) {
                    val newLetter = getRandomLetter().toString()
                    response = homeRepository.searchByFirstLetter(newLetter)
                }

                _searchedMeal.value = response
            } else {
                AuthHelper.getUserID(context)?.let { userId ->
                    val response = AppDatabase.getDatabase(context).recipeDao().getAllRecipes(userId)
                    _searchedMeal.value = MyResponse(response.map { it.meal!! })
                }
            }
        }
    }


    private fun getCategories(connectivityStatus: ConnectivityObserver.Status) {
        viewModelScope.launch {
            if (connectivityStatus == ConnectivityObserver.Status.Available) {
                val response = homeRepository.getCategories()
                _categories.value = response
            } else {
                _categories.value = Category(emptyList())
            }
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

    private suspend fun getMealByID(mealID: String): Meal {
        return homeRepository.getMealByID(mealID).meals[0]
    }

    private fun getRandomLetter(): Char {
        return ('a'..'z').random()
    }
}