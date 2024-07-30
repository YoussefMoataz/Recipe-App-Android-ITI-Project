package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.view.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ymoataz.iti.android.recipe_app_android_iti_project.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_splash, container, false)

        lifecycleScope.launch {
            delay(3000)
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        }

        return view
    }

}