package com.ymoataz.iti.android.recipe_app_android_iti_project.auth.view

import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.ymoataz.iti.android.recipe_app_android_iti_project.R
import com.ymoataz.iti.android.recipe_app_android_iti_project.User
import com.ymoataz.iti.android.recipe_app_android_iti_project.UserViewModel
import com.ymoataz.iti.android.recipe_app_android_iti_project.UserViewModelFactory
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.AppDatabase
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.LocalDataSourceImpl
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.UserDao
import com.ymoataz.iti.android.recipe_app_android_iti_project.repo.UserRepoImpl

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


// Check if the fragment_container is present (necessary for two-pane layout support)
        if (findViewById<FrameLayout>(R.id.fragment_container) != null) {
            // Check if we're being restored from a previous state, otherwise add fragment
            if (savedInstanceState != null) {
                return
            }

            // Create an instance of ExampleFragment
            val exampleFragment = LoginFragment()

            // Add the fragment to the fragment_container FrameLayout
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, exampleFragment)
                .commit()
        }

    }
}