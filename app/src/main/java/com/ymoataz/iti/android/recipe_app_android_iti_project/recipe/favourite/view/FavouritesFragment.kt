package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.favourite.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ymoataz.iti.android.recipe_app_android_iti_project.R
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.AppDatabase
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.RecipeLocalDataSourceImp
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.favourite.repo.FavouriteRepoImp
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.favourite.viewModel.FavouriteViewModel
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.favourite.viewModel.FavouriteViewModelFactory


class FavouritesFragment : Fragment() {

    private lateinit var favouriteViewModel: FavouriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourites, container, false)


        favouriteViewModel = FavouriteViewModelFactory(
            FavouriteRepoImp(
                RecipeLocalDataSourceImp(
                    AppDatabase.getDatabase(requireContext()).recipeDao()
                )
            )
        ).create(FavouriteViewModel::class.java)

        favouriteViewModel.getAllRecipes(1)

        favouriteViewModel.favouriteRecipes.observe(viewLifecycleOwner){
            view.findViewById<TextView>(R.id.favourites_fragment_text).text =
                it?.get(0)?.meal?.strMeal ?: "hello"
        }

        return view
    }

}