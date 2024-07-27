package com.ymoataz.iti.android.recipe_app_android_iti_project.auth.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.LocalDataSourceImpl
import com.ymoataz.iti.android.recipe_app_android_iti_project.R
import com.ymoataz.iti.android.recipe_app_android_iti_project.repo.UserRepoImpl
import com.ymoataz.iti.android.recipe_app_android_iti_project.UserViewModel
import com.ymoataz.iti.android.recipe_app_android_iti_project.UserViewModelFactory
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.AppDatabase

class LoginFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_login, container, false)
        val userDao = AppDatabase.getDatabase(requireContext()).userDao()
        val localDataSource = LocalDataSourceImpl(userDao)
        val userRepository = UserRepoImpl(localDataSource)
        val userViewModelFactory = UserViewModelFactory(userRepository)
        userViewModel= ViewModelProvider(this,userViewModelFactory).get(UserViewModel::class.java)
        view.findViewById<Button>(R.id.btnLogin).setOnClickListener {
            val email = view.findViewById<EditText>(R.id.editTextEmailAddress).text.toString()
            val password = view.findViewById<EditText>(R.id.editTextPassword).text.toString()
            userViewModel.login(email, password)
        }
        userViewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                Log.d("asd->>", "successsss!!")
            } else {
                Log.d("asd->>", "Faileddd!!")
            }
        }
        return view
    }
}