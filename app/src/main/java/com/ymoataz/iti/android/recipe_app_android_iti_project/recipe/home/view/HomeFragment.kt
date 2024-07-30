package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.home.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ymoataz.iti.android.recipe_app_android_iti_project.R
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.AppDatabase
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.adapter.MyAdapter
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.Recipe
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.RecipeLocalDataSourceImp
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.favourite.repo.FavouriteRepoImp
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.favourite.viewModel.FavouriteViewModel
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.favourite.viewModel.FavouriteViewModelFactory
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.home.repo.HomeRepositoryImp
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.home.viewModel.HomeViewModel
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.home.viewModel.HomeViewModelFactory
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network.APIClient
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), MyAdapter.OnFavouriteIconClickListener {
    private lateinit var viewModel: HomeViewModel

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val mainCard= view.findViewById<View>(R.id.single_card)
        val cardImage= view.findViewById<ImageView>(R.id.recipeImageView)
        val cardTitle= view.findViewById<TextView>(R.id.recipeTitleTextView)
        val cardCategory= view.findViewById<TextView>(R.id.recipeCategoryTextView)
        val favouriteIcon= view.findViewById<ImageButton>(R.id.favoriteIcon)
        var isFavourite = false
        favouriteIcon.setOnClickListener{
            updateFavoriteIcon(favouriteIcon, isFavourite)
            isFavourite = !isFavourite
        }
        gettingViewModelReady()
        viewModel.getRandomMeal()
        viewModel.randomMeal.observe(viewLifecycleOwner) {
            cardTitle.text = it.meals[0].strMeal
            cardCategory.text = it.meals[0].strCategory
            Glide.with(this).load(it.meals[0].strMealThumb).into(cardImage)
        }
        val rv= view.findViewById<RecyclerView>(R.id.home_recycle_view)
        var randomChar= getRandomLetter()
        viewModel.searchByFirstLetter(randomChar.toString())
        viewModel.searchedMeal.observe(viewLifecycleOwner) {
                searchResult ->
            val data = searchResult?.meals ?: emptyList()
            var recipe = data.map { Recipe(0, 1, it, false) }
            if (recipe.isEmpty()) {
                recipe = emptyList()
            }
            val adapter= MyAdapter(recipe, requireContext(), object : MyAdapter.OnRecipeItemClickListener {
                override fun onClick(position: Int, recipe: Recipe) {
                    val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(recipe)
                    findNavController().navigate(action)
                }
            }, this)
            rv.layoutManager = LinearLayoutManager(requireContext())
            rv.adapter = adapter
            adapter.notifyDataSetChanged()
        }
        mainCard.setOnClickListener{
            viewModel.randomMeal.value?.meals?.get(0)?.let {
                val recipe = Recipe(0, 1, it, isFavourite)
                val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(recipe)
                findNavController().navigate(action)
            }
        }
        return view
    }
    private fun gettingViewModelReady(){
        val ProductsViewModelFactory=HomeViewModelFactory(
            repository = HomeRepositoryImp(
                remoteDataSource = APIClient
            )
        )
        viewModel= ViewModelProvider(this,ProductsViewModelFactory).get(HomeViewModel::class.java)
    }
    private fun updateFavoriteIcon(favouriteIcon: ImageButton, isFavourite: Boolean) {
        run {
            if (isFavourite) {
               favouriteIcon.setImageResource(R.drawable.baseline_favorite_24)
            } else {
//                lifecycleScope.launch {
//
//                    AppDatabase.getDatabase(requireContext()).recipeDao().insertRecipe(Recipe(0, 1, viewModel.randomMeal.value?.meals?.get(0), true))
//                }
//                Toast.makeText(requireContext(), "Added", Toast.LENGTH_SHORT).show()

                favouriteIcon.setImageResource(R.drawable.baseline_favorite_border_24)
            }

        }
    }
    fun getRandomLetter(): Char {
        return ('a'..'z').random()
    }

    override fun onClick(isFavourite: Boolean, recipe: Recipe) {
        if (isFavourite){
            lifecycleScope.launch {
                AppDatabase.getDatabase(requireContext()).recipeDao().deleteRecipe(recipe)
            }
        }else{
            lifecycleScope.launch {
                AppDatabase.getDatabase(requireContext()).recipeDao().insertRecipe(recipe)
            }
        }
    }
}