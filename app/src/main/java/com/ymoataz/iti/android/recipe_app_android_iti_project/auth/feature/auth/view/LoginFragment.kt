package com.ymoataz.iti.android.recipe_app_android_iti_project.auth.feature.auth.view

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.core.local.data_sources.impl.UserLocalDataSourceImpl
import com.ymoataz.iti.android.recipe_app_android_iti_project.R
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.core.common.AuthHelper
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.core.local.repo.UserRepoImpl
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.feature.auth.viewModel.UserViewModel
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.feature.auth.viewModel.UserViewModelFactory
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.AppDatabase

class LoginFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel
    private val args: LoginFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_login, container, false)

        if (args.isFromLogout) {
            activity?.onBackPressedDispatcher?.addCallback(requireActivity(),
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        activity?.finish()
                    }
                })
        }

        val userDao = AppDatabase.getDatabase(requireContext()).userDao()
        val localDataSource = UserLocalDataSourceImpl(userDao)
        val userRepository = UserRepoImpl(localDataSource)
        val userViewModelFactory = UserViewModelFactory(userRepository)
        userViewModel= ViewModelProvider(this,userViewModelFactory).get(UserViewModel::class.java)
        val emailEditText=view.findViewById<EditText>(R.id.editTextEmailAddress)
        val passwordEditText=view.findViewById<EditText>(R.id.editTextPassword)
        val regBtn=view.findViewById<Button>(R.id.btnRegister)
        addTextWatcher(emailEditText)
        addTextWatcher(passwordEditText)
        //userViewModel.clearUsers()
        regBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        view.findViewById<Button>(R.id.btnLogin).setOnClickListener {
            var allFieldsFilled = true

            if (emailEditText.text.toString().isEmpty()) {
                setBorder(emailEditText, R.color.red)
                allFieldsFilled = false
            }
            if (passwordEditText.text.toString().isEmpty()) {
                setBorder(passwordEditText, R.color.red)
                allFieldsFilled = false
            }
            if (!allFieldsFilled) {
                Toast.makeText(
                    activity,
                    "Please fill all the required information!",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            userViewModel.login(email, password)

        }

        userViewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            val email = emailEditText.text.toString()
            if (isSuccess) {
                Log.d("asd->>", "successsss!!")
                userViewModel.getUserIdByEmail(email)
                userViewModel.userIdByEmail.observe(viewLifecycleOwner){userID->
                    context?.let { AuthHelper.saveUserDataInSP(it,userID) }
                }
                findNavController().navigate(R.id.action_loginFragment_to_recipeActivity)
                activity?.finish()
            } else {
                Log.d("asd->>", "Faileddd!!")
                Toast.makeText(activity,"Wrong email or password!",Toast.LENGTH_LONG).show()
            }
        }
        return view
    }
    private fun addTextWatcher(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    setBorder(editText, R.color.red)
                } else {
                    setBorder(editText, R.color.gray)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setBorder(editText: EditText, color: Int) {
        val drawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 70f
            setStroke(3, ContextCompat.getColor(requireContext(), color))
        }
        editText.background = drawable
    }
}