package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.search.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network.dto.MyResponse
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.search.repo.SearchRepository
import kotlinx.coroutines.launch

class SearchViewModel (private val searchRepository: SearchRepository): ViewModel(){
    private val _searchResult= MutableLiveData<MyResponse>()
    val searchResult : LiveData<MyResponse> = _searchResult

    fun search(mealName : String){
        viewModelScope.launch {
            val response = searchRepository.search(mealName)
            _searchResult.value = response
        }
    }

}