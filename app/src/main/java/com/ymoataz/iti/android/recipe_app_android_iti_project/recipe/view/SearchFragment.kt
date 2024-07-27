package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ymoataz.iti.android.recipe_app_android_iti_project.R
import androidx.navigation.fragment.findNavController



class SearchFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)

//        val homeScreenText=view.findViewById<TextView>(R.id.home_fragment_text)
//        val favouritesScreenText=view.findViewById<TextView>(R.id.favourites_fragment_text)
//        homeScreenText.setOnClickListener {
//            findNavController().navigate(R.id.action_searchFragment_to_homeFragment)
//        }
//        favouritesScreenText.setOnClickListener {
//            findNavController().navigate(R.id.action_searchFragment_to_favouritesFragment)
//        }
        return view
    }
}