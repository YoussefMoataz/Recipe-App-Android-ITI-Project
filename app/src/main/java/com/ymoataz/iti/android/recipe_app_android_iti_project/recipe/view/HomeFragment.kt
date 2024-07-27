package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.ymoataz.iti.android.recipe_app_android_iti_project.R


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
//        val searchScreenText:TextView=view.findViewById(R.id.search_fragment_text)
//        val favouritesScreenText:TextView=view.findViewById(R.id.favourites_fragment_text)
//        searchScreenText.setOnClickListener {
//            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
//        }
//        favouritesScreenText.setOnClickListener {
//            findNavController().navigate(R.id.action_homeFragment_to_favouritesFragment)
//        }
        return view
    }


}