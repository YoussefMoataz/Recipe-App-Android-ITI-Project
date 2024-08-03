package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.about_us.view

import Person
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ymoataz.iti.android.recipe_app_android_iti_project.R
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.about_us.adapter.PersonAdapter

class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_about, container, false)

        val rv= view.findViewById<RecyclerView>(R.id.recyclerViewAbout)
        rv.layoutManager = LinearLayoutManager(this.context)
        val personList = listOf(
            Person(R.drawable.person_youssef, "Youssef Moataz", "https://github.com/YoussefMoataz"),
            Person(R.drawable.person_ahmed, "Ahmed Salah", "https://github.com/ahmed-sala"),
            Person(R.drawable.person_bassel, "Bassel Islam", "https://github.com/bassel-islam"),
            Person(R.drawable.person_badr, "Badr Mohamed", "https://github.com/Badrmohamedragab")
        )
        rv.adapter = PersonAdapter(personList)



        return view

    }

}