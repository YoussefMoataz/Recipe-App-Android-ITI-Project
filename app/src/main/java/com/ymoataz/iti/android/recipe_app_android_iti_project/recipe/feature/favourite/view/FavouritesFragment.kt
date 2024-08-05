package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.favourite.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.snackbar.Snackbar

import com.ymoataz.iti.android.recipe_app_android_iti_project.R
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.core.common.AuthHelper
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.AppDatabase
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.local.entities.Recipe
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.local.data_sources.impl.RecipeLocalDataSourceImpl
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.common.adapters.RecipesRecyclerViewAdapter
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.favourite.repo.FavouriteRepoImp
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.favourite.viewModel.FavouriteViewModel
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.favourite.viewModel.FavouriteViewModelFactory


class FavouritesFragment : Fragment(),
    RecipesRecyclerViewAdapter.OnRecipeItemClickListener,
    RecipesRecyclerViewAdapter.OnFavouriteIconClickListener {
    private lateinit var noInternetAnimation: LottieAnimationView
    private lateinit var loadingAnimation: LottieAnimationView
    private lateinit var favouriteViewModel: FavouriteViewModel
    private lateinit var favouritesRecyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourites, container, false)

        favouriteViewModel = FavouriteViewModelFactory(
            FavouriteRepoImp(
                RecipeLocalDataSourceImpl(
                    AppDatabase.getDatabase(requireContext()).recipeDao()
                )
            )
        ).create(FavouriteViewModel::class.java)

        favouritesRecyclerView = view.findViewById(R.id.favourites_recycler_view)
        noInternetAnimation = view.findViewById(R.id.no_internet_animation)
        loadingAnimation = view.findViewById(R.id.loading_food_animation)
        AuthHelper.getUserID(requireContext())?.let { favouriteViewModel.getAllRecipes(it) }

        favouritesRecyclerView.layoutManager = LinearLayoutManager(view.context)

        val favouriteAdapter = RecipesRecyclerViewAdapter(emptyList(), view.context, this, this)
        favouritesRecyclerView.adapter = favouriteAdapter

        favouriteViewModel.favouriteRecipes.observe(viewLifecycleOwner) { recipe ->
            recipe?.let {
                favouriteAdapter.updateData(it)
                if (it.isEmpty()) {
                    showNoInternetAnimation()
                    return@observe
                }
                hideNoInternetAnimation()
            }
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val recipeToBeDeleted =
                    favouriteViewModel.favouriteRecipes.value?.get(viewHolder.adapterPosition)

                recipeToBeDeleted?.let { recipe ->
                    favouriteViewModel.deleteRecipe(recipe)

                    Snackbar.make(
                        favouritesRecyclerView,
                        "Deleted " + recipe.meal?.strMeal,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction("Undo") {
                            favouriteViewModel.insertRecipe(recipe)
                        }
                        .show()
                }
            }

        }).attachToRecyclerView(favouritesRecyclerView)

        return view
    }

    override fun onClick(position: Int, recipe: Recipe) {
        val action = FavouritesFragmentDirections.actionFavouritesFragmentToDetailsFragment(recipe)
        findNavController().navigate(action)
    }
    private fun showNoInternetAnimation() {
        noInternetAnimation.visibility = View.VISIBLE
        favouritesRecyclerView.visibility = View.GONE
        loadingAnimation.visibility = View.GONE
    }

    private fun hideNoInternetAnimation() {
        noInternetAnimation.visibility = View.GONE
        favouritesRecyclerView.visibility = View.VISIBLE
        loadingAnimation.visibility = View.GONE
    }
    override fun onClick(isFavourite: Boolean, recipe: Recipe) {
        if (isFavourite) {
            favouriteViewModel.deleteRecipe(recipe)
//            AuthHelper.getUserID(requireContext())?.let { favouriteViewModel.getAllRecipes(it) }
        }
    }

}