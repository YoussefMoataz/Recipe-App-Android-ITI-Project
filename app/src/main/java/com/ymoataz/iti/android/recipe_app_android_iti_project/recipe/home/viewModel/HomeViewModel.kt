package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.home.repo.HomeRepository
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network.dto.MyResponse
import kotlinx.coroutines.launch

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {

    private val _randomMeal = MutableLiveData<MyResponse>()
    val randomMeal: LiveData<MyResponse> = _randomMeal
    private val _searchedMeal = MutableLiveData<MyResponse>()
    val searchedMeal: LiveData<MyResponse> = _searchedMeal
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
}