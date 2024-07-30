package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.favourite.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.ymoataz.iti.android.recipe_app_android_iti_project.R
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.AuthHelper
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.AppDatabase
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.Recipe
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.RecipeLocalDataSourceImp
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.adapter.MyAdapter
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.favourite.repo.FavouriteRepoImp
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.favourite.viewModel.FavouriteViewModel
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.favourite.viewModel.FavouriteViewModelFactory


class FavouritesFragment : Fragment(),
    MyAdapter.OnRecipeItemClickListener,
    MyAdapter.OnFavouriteIconClickListener {

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

        AuthHelper.getUserID(requireContext())?.let { favouriteViewModel.getAllRecipes(it) }

        favouritesRecyclerView.layoutManager = LinearLayoutManager(view.context)

        val favouriteAdapter = MyAdapter(emptyList(), view.context, this, this)
        favouritesRecyclerView.adapter = favouriteAdapter

        favouriteViewModel.favouriteRecipes.observe(viewLifecycleOwner){ recipe ->
            recipe?.let {
                favouriteAdapter.updateData(it)
            }
        }

        return view
    }

    override fun onClick(position: Int, recipe: Recipe) {
        val action = FavouritesFragmentDirections.actionFavouritesFragmentToDetailsFragment(recipe)
        findNavController().navigate(action)
    }

    override fun onClick(isFavourite: Boolean, recipe: Recipe) {
        if (isFavourite){
            favouriteViewModel.deleteRecipe(recipe)
            AuthHelper.getUserID(requireContext())?.let { favouriteViewModel.getAllRecipes(it) }
        }
    }

}