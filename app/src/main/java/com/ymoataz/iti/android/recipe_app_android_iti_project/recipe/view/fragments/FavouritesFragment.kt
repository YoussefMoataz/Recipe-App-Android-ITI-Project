package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ymoataz.iti.android.recipe_app_android_iti_project.R


class FavouritesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourites, container, false)

//        val homeScreenText=view.findViewById<TextView>(R.id.home_fragment_text)
//        val searchScreenText=view.findViewById<TextView>(R.id.search_fragment_text)
//        homeScreenText.setOnClickListener {
//            findNavController().navigate(R.id.action_favouritesFragment_to_homeFragment)
//        }
//        searchScreenText.setOnClickListener {
//            findNavController().navigate(R.id.action_favouritesFragment_to_searchFragment)
//        }
        return view
    }

}