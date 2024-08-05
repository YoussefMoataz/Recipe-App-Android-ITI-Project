package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.search.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.core.common.AuthHelper
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.AppDatabase
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.common.connectivity.ConnectivityObserver
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.common.connectivity.NetworkConnectivityObserver
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network.models.meals.MyResponse
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.search.repo.SearchRepository
import kotlinx.coroutines.launch

class SearchViewModel (private val searchRepository: SearchRepository ,val context: Context): ViewModel(){
    private val _searchResult= MutableLiveData<MyResponse>()
    val searchResult : LiveData<MyResponse> = _searchResult

    private val _connectionStatus = MutableLiveData<ConnectivityObserver.Status>()
    val connectionStatus : LiveData<ConnectivityObserver.Status> = _connectionStatus

    init {
        val connectivityObserver = NetworkConnectivityObserver(context)
        viewModelScope.launch {
            connectivityObserver.observe().collect{ status->
                _connectionStatus.value = status
            }
        }

    }

    fun search( mealName : String) {
        viewModelScope.launch {
            if (connectionStatus.value == ConnectivityObserver.Status.Available) {
                val response = searchRepository.search(mealName)
                _searchResult.value = response
            } else {
                AuthHelper.getUserID(context)?.let { userId ->

                        val response =
                            AppDatabase.getDatabase(context).recipeDao().getAllRecipes(userId)
                        val recipes = MyResponse(response.map { it.meal!! })
                        val myResponse = MyResponse(recipes.meals.filter {
                            it.strMeal?.contains(mealName)!!


                        })
                        _searchResult.value = myResponse
                }
            }
        }
    }
}