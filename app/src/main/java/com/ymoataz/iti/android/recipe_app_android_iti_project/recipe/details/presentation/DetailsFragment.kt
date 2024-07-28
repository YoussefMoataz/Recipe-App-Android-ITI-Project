package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.details.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.ymoataz.iti.android.recipe_app_android_iti_project.R

class DetailsFragment : Fragment() {

    val args: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_details, container, false)

//        val rec = Recipe("Corba", "https://www.themealdb.com/images/media/meals/58oia61564916529.jpg", false)

//        view.findViewById<TextView>(R.id.recipe_name_text_view).text = rec.title

        val recipe = args.recipe

        Glide.with(this).load(recipe.meal?.strMealThumb)
            .into(view.findViewById(R.id.recipe_thumb_image_view))
        view.findViewById<TextView>(R.id.recipe_name_text_view).text = recipe.meal?.strMeal

        return view
    }
}