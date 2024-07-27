package com.ymoataz.iti.android.recipe_app_android_iti_project.auth.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.ymoataz.iti.android.recipe_app_android_iti_project.R
import com.ymoataz.iti.android.recipe_app_android_iti_project.User
import com.ymoataz.iti.android.recipe_app_android_iti_project.UserViewModel
import com.ymoataz.iti.android.recipe_app_android_iti_project.UserViewModelFactory
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.AppDatabase
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.LocalDataSourceImpl
import com.ymoataz.iti.android.recipe_app_android_iti_project.repo.UserRepoImpl


class RegisterFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var fname:EditText
    private lateinit var lname:EditText
    private lateinit var email:EditText
    private lateinit var password:EditText
    private lateinit var regBtn:Button
    private lateinit var user: User
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view= inflater.inflate(R.layout.fragment_register, container, false)
        val userDao = AppDatabase.getDatabase(requireContext()).userDao()
        val localDataSource = LocalDataSourceImpl(userDao)
        val userRepository = UserRepoImpl(localDataSource)
        val userViewModelFactory = UserViewModelFactory(userRepository)
        userViewModel= ViewModelProvider(this,userViewModelFactory).get(UserViewModel::class.java)
        fname=view.findViewById(R.id.editTextFirstName)
        lname=view.findViewById(R.id.editTextLastName)
        email=view.findViewById(R.id.editTextEmailInRegister)
        password=view.findViewById(R.id.editTextPasswordInRegister)
        regBtn=view.findViewById(R.id.btnRegInRegister)

        regBtn.setOnClickListener {
            user=User(fName = fname.text.toString(), lName = lname.text.toString(), email = email.text.toString(),
                password =password.text.toString())
            userViewModel.register(user)
        }
        return view
    }
}