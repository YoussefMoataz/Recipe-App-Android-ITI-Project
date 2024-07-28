package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.search.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ymoataz.iti.android.recipe_app_android_iti_project.R
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.adapter.MyAdapter
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.adapter.Recipe


class SearchFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val data : List<Recipe> = generateCakeList()
        super.onCreate(savedInstanceState)
        val rv = view.findViewById<RecyclerView>(R.id.searchRecycleView)
        rv.layoutManager = LinearLayoutManager(view.context)
        rv.adapter = MyAdapter(data, view.context)
        return view
    }
    private fun generateCakeList(): List<Recipe> {
        return listOf(
            Recipe("Corba", "https://www.themealdb.com/images/media/meals/58oia61564916529.jpg", false),
            Recipe("Tamiya", "https://www.themealdb.com/images/media/meals/n3xxd91598732796.jpg", true),
            Recipe("Lasagne", "https://www.themealdb.com/images/media/meals/wtsvxx1511296896.jpg", false),
            Recipe("Kafteji", "https://www.themealdb.com/images/media/meals/1bsv1q1560459826.jpg", true),
            Recipe("Dal fry", "https://www.themealdb.com/images/media/meals/wuxrtu1483564410.jpg", true),
             Recipe("Corba", "https://www.themealdb.com/images/media/meals/58oia61564916529.jpg", false),
            Recipe("Tamiya", "https://www.themealdb.com/images/media/meals/n3xxd91598732796.jpg", true),
            Recipe("Lasagne", "https://www.themealdb.com/images/media/meals/wtsvxx1511296896.jpg", false),
            Recipe("Kafteji", "https://www.themealdb.com/images/media/meals/1bsv1q1560459826.jpg", true),
            Recipe("Dal fry", "https://www.themealdb.com/images/media/meals/wuxrtu1483564410.jpg", true),

        )
    }
}
