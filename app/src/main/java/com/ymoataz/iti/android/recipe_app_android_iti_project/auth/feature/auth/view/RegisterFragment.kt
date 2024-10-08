package com.ymoataz.iti.android.recipe_app_android_iti_project.auth.feature.auth.view

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ymoataz.iti.android.recipe_app_android_iti_project.R
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.core.common.AuthHelper
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.core.local.repo.UserRepoImpl
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.feature.auth.viewModel.UserViewModel
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.feature.auth.viewModel.UserViewModelFactory
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.AppDatabase
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.core.local.data_sources.impl.UserLocalDataSourceImpl
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.core.local.entities.User
import java.util.regex.Pattern


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

        activity?.onBackPressedDispatcher?.addCallback(requireActivity(),
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_global_loginFragment)
                }
            })

        val userDao = AppDatabase.getDatabase(requireContext()).userDao()
        val localDataSource = UserLocalDataSourceImpl(userDao)
        val userRepository = UserRepoImpl(localDataSource)
        val userViewModelFactory = UserViewModelFactory(userRepository)
        userViewModel= ViewModelProvider(this,userViewModelFactory).get(UserViewModel::class.java)
        fname=view.findViewById(R.id.editTextFirstName)
        lname=view.findViewById(R.id.editTextLastName)
        email=view.findViewById(R.id.editTextEmailInRegister)
        password=view.findViewById(R.id.editTextPasswordInRegister)
        regBtn=view.findViewById(R.id.btnRegInRegister)
        //val isEmailValid=isEmailValid(email.text.toString())

        addTextWatcher(fname)
        addTextWatcher(lname)
        addTextWatcher(email)
        addTextWatcher(password)
        //userViewModel.clearUsers()

        regBtn.setOnClickListener {
            var allFieldsFilled = true
            var isValidPass=isPasswordValid(password.text.toString())
            var isEmailValid=isEmailValid(email.text.toString())

            if (fname.text.toString().isEmpty()) {
                setBorder(fname, R.color.red)
                allFieldsFilled = false
            }
            if (lname.text.toString().isEmpty()) {
                setBorder(lname, R.color.red)
                allFieldsFilled = false
            }
            if (email.text.toString().isEmpty()) {
                setBorder(email, R.color.red)
                allFieldsFilled = false
            }
            if (password.text.toString().isEmpty()) {
                setBorder(password, R.color.red)
                allFieldsFilled = false
            }

            if (!allFieldsFilled) {
                Toast.makeText(activity, "Please fill all the required information!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (!isEmailValid){
                Toast.makeText(activity, "This is not a valid email!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(!isValidPass){
               /* Toast.makeText(activity,
                    "Contain at least one digit.\n" +
                            "Contain at least one lowercase letter.\n" +
                            "Contain at least one uppercase letter.\n" +
                            "Contain at least one special character from the specified set.\n" +
                            "Have no whitespace characters.\n" +
                            "Be at least 4 characters long.!",
                    Toast.LENGTH_LONG).show()*/
                showPasswordCriteriaDialog()
                return@setOnClickListener
            }
            userViewModel.checkIfEmailExistBefore(email.text.toString())
        }
        userViewModel.isExistBefore.observe(viewLifecycleOwner) { isEmailExist ->
            if (isEmailExist) {
                Toast.makeText(activity, "This email has been registered before!", Toast.LENGTH_LONG).show()
            } else {
                 user = User(
                    fName = fname.text.toString(),
                    lName = lname.text.toString(),
                    email = email.text.toString(),
                    password = password.text.toString()
                )
                userViewModel.register(user)
                Toast.makeText(activity, "Registration successful!", Toast.LENGTH_LONG).show()
                userViewModel.getUserIdByEmail(user.email)
                userViewModel.userIdByEmail.observe(viewLifecycleOwner){userID->
                    context?.let { AuthHelper.saveUserDataInSP(it,userID) }
                }
                findNavController().navigate(R.id.action_registerFragment_to_recipeActivity)
                activity?.finish()
            }
        }
        return view
    }
    private fun isEmailValid(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
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
    fun isPasswordValid(password: String): Boolean {
        val pattern: Pattern

        val regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"

        pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(password)

        return matcher.matches()
    }
    private fun showPasswordCriteriaDialog() {
        val passwordCriteria = """
            Password must:
            - Contain at least one digit.
            - Contain at least one lowercase letter.
            - Contain at least one uppercase letter.
            - Contain at least one special character from @#$%^&+=.
            - Have no whitespace characters.
            - Be at least 4 characters long.
        """.trimIndent()

        val dialog = activity?.let { MaterialAlertDialogBuilder(it).create() }
        if (dialog != null) {
            dialog.setMessage(passwordCriteria)
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { dialogInterface, _ ->
                dialogInterface.dismiss()}
            dialog.show()
        }

    }

}