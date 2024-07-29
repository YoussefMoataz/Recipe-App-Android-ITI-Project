package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.details.presentation.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.colormoon.readmoretextview.ReadMoreTextView
import com.ymoataz.iti.android.recipe_app_android_iti_project.R
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.details.helpers.IngredientsHelper
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.details.presentation.adapters.DetailsIngredientsAdapter

class DetailsFragment : Fragment() {

    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_details, container, false)

        val recipe = args.recipe

        if (recipe?.meal != null) {
            Glide.with(this).load(recipe.meal.strMealThumb)
                .into(view.findViewById(R.id.recipe_thumb_image_view))
            view.findViewById<TextView>(R.id.recipe_name_text_view).text = recipe.meal.strMeal
            view.findViewById<TextView>(R.id.recipe_area_text_view).text = recipe.meal.strArea
            view.findViewById<TextView>(R.id.recipe_category_text_view).text =
                recipe.meal.strCategory
            view.findViewById<ReadMoreTextView>(R.id.recipe_instructions_text_view).text =
                recipe.meal.strInstructions

            val ingredientsRecyclerView =
                view.findViewById<RecyclerView>(R.id.recipe_ingredients_recycler_view)

            val ingredientsAdapter = DetailsIngredientsAdapter(
                requireActivity(),
                IngredientsHelper.getNonEmptyIngredientsAndMeasures(recipe.meal)
            )

            ingredientsRecyclerView.adapter = ingredientsAdapter
            ingredientsRecyclerView.layoutManager = LinearLayoutManager(activity)

        }

        return view
    }
}