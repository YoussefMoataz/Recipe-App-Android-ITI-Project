package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.favourite.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ymoataz.iti.android.recipe_app_android_iti_project.R
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.AppDatabase
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.Recipe
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.RecipeLocalDataSourceImp
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.adapter.MyAdapter
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.favourite.repo.FavouriteRepoImp
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.favourite.viewModel.FavouriteViewModel
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.favourite.viewModel.FavouriteViewModelFactory
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.search.view.SearchFragmentDirections


class FavouritesFragment : Fragment(), MyAdapter.OnRecipeItemClickListener {

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

        val favouritesRecyclerView = view.findViewById<RecyclerView>(R.id.favourites_recycler_view)

        favouriteViewModel.getAllRecipes(1)

        favouritesRecyclerView.layoutManager = LinearLayoutManager(view.context)

        favouriteViewModel.favouriteRecipes.observe(viewLifecycleOwner){ recipe ->
            recipe?.let {
                favouritesRecyclerView.adapter = MyAdapter(it, view.context, this)
            }
        }

        return view
    }

    override fun onClick(position: Int, recipe: Recipe) {
        val action = FavouritesFragmentDirections.actionFavouritesFragmentToDetailsFragment(recipe)
        findNavController().navigate(action)
    }
}