package com.ymoataz.iti.android.recipe_app_android_iti_project.about_us.view

import Person
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ymoataz.iti.android.recipe_app_android_iti_project.R
import com.ymoataz.iti.android.recipe_app_android_iti_project.about_us.adapter.PersonAdapter

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
            Person(R.drawable.person1, "John Doe", "https://github.com/johndoe"),
            Person(R.drawable.person1, "Jane Smith", "https://github.com/janesmith"),
            Person(R.drawable.person1, "James Brown", "https://github.com/jamesbrown"),
            Person(R.drawable.person1, "Emily Davis", "https://github.com/emilydavis")
        )
        rv.adapter = PersonAdapter(personList)



        return view

    }

}