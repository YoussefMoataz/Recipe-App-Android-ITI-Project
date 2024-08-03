package com.ymoataz.iti.android.recipe_app_android_iti_project.auth.feature.auth.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.core.local.entities.User
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.core.repo.UserRepo
import kotlinx.coroutines.launch

class UserViewModel(private val repo: UserRepo):ViewModel() {
    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean>
        get() = _isSuccess

    private val _isExistBefore = MutableLiveData<Boolean>()
    val isExistBefore: LiveData<Boolean>
        get() = _isExistBefore

    private val _userIdByEmail=MutableLiveData<Int>()
    val userIdByEmail:LiveData<Int>
        get() = _userIdByEmail

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

    fun checkIfEmailExistBefore(email: String){
        viewModelScope.launch {
            _isExistBefore.postValue(repo.checkEmailIfExistBefore(email))
        }
    }

    fun clearUsers(){
        viewModelScope.launch {
            repo.clearUsers()
        }
    }

    fun getUserIdByEmail(email: String){
        viewModelScope.launch {
            _userIdByEmail.postValue(repo.getUserIdByEmail(email))
        }
    }

}