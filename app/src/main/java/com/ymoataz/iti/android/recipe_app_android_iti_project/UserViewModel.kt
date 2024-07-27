package com.ymoataz.iti.android.recipe_app_android_iti_project

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ymoataz.iti.android.recipe_app_android_iti_project.repo.UserRepo
import kotlinx.coroutines.launch

class UserViewModel(private val repo: UserRepo):ViewModel() {
    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean>
        get() = _isSuccess

    fun register(user: User){
        viewModelScope.launch {
            repo.insertUser(user)
        }
    }

    fun login(email:String,password:String){
        viewModelScope.launch {
            _isSuccess.postValue(repo.login(email, password))
        }
    }
}