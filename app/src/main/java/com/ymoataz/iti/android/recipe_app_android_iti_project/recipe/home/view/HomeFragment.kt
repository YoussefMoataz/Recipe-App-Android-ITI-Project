package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.home.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ymoataz.iti.android.recipe_app_android_iti_project.R
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.AuthHelper
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.AppDatabase
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.Recipe
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.adapter.MyAdapter
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.home.repo.HomeRepositoryImp
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.home.viewModel.HomeViewModel
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.home.viewModel.HomeViewModelFactory
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network.APIClient
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), MyAdapter.OnRecipeItemClickListener,
    MyAdapter.OnFavouriteIconClickListener {

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: MyAdapter
    private lateinit var rv: RecyclerView

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Handle back press
        handleOnBackPressed()

        val mainCard = view.findViewById<View>(R.id.single_card)
        val cardImage = view.findViewById<ImageView>(R.id.recipeImageView)
        val cardTitle = view.findViewById<TextView>(R.id.recipeTitleTextView)
        val cardCategory = view.findViewById<TextView>(R.id.recipeCategoryTextView)
        val favouriteIcon = view.findViewById<ImageButton>(R.id.favoriteIcon)

        // Initialize RecyclerView and Adapter
        rv = view.findViewById(R.id.home_recycle_view)
        rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv.adapter = MyAdapter(emptyList(), view.context, this, this)
        rv.setHasFixedSize(true)
        adapter = rv.adapter as MyAdapter

        rv.adapter = adapter
        gettingViewModelReady()
        viewModel.getRandomMeal()
        viewModel.randomMeal.observe(viewLifecycleOwner) { mealResponse ->
            val meal = mealResponse.meals[0]
            cardTitle.text = meal.strMeal
            cardCategory.text = meal.strCategory
            Glide.with(this).load(meal.strMealThumb).into(cardImage)

            lifecycleScope.launch {
                val userId = AuthHelper.getUserID(requireContext())
                val recipes = userId?.let { AppDatabase.getDatabase(requireContext()).recipeDao().getAllRecipes(it) }
                val isFavourite = recipes?.any { recipe -> recipe.meal?.idMeal == meal.idMeal } ?: false
                updateFavoriteIcon(favouriteIcon, isFavourite)
            }
        }

        favouriteIcon.setOnClickListener {
            lifecycleScope.launch {
                viewModel.randomMeal.value?.meals?.get(0)?.let { meal ->
                    val userId = AuthHelper.getUserID(requireContext())
                    val currentIsFavourite = userId?.let { id ->
                        AppDatabase.getDatabase(requireContext()).recipeDao().getAllRecipes(id)?.any { recipe ->
                            recipe.meal?.idMeal == meal.idMeal
                        } ?: false
                    } ?: false

                    if (currentIsFavourite) {
                        AppDatabase.getDatabase(requireContext()).recipeDao().deleteRecipeWithMeal(meal)
                    } else {
                        userId?.let { id ->
                            val recipe = Recipe(0, id, meal, true)
                            AppDatabase.getDatabase(requireContext()).recipeDao().insertRecipe(recipe)
                        }
                    }
                    updateFavoriteIcon(favouriteIcon, !currentIsFavourite)
                }
            }
        }

        val randomChar = getRandomLetter()
        viewModel.searchByFirstLetter(randomChar.toString())
        viewModel.searchedMeal.observe(viewLifecycleOwner) { searchResult ->
            val data = searchResult?.meals ?: emptyList()
            lifecycleScope.launch {
                val userId = AuthHelper.getUserID(requireContext())
                val recipes = userId?.let { AppDatabase.getDatabase(requireContext()).recipeDao().getAllRecipes(it) }
                val recipeList = data.map { meal ->
                    val isFavourite = recipes?.any { recipe -> recipe.meal?.idMeal == meal.idMeal } ?: false
                    Recipe(0, userId, meal, isFavourite)
                }

                if (recipeList.isEmpty()) {
                    refreshScreen()
                } else {
                    adapter.updateData(recipeList)
                    adapter.notifyDataSetChanged()
                }
            }
        }

        mainCard.setOnClickListener {
            viewModel.randomMeal.value?.meals?.get(0)?.let {
                val recipe = Recipe(0, 1, it, false)
                val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(recipe)
                findNavController().navigate(action)
            }
        }
        return view
    }

    private fun refreshScreen() {
        // Perform the refresh action, such as reloading data or showing a message
        val randomChar = getRandomLetter()
        viewModel.searchByFirstLetter(randomChar.toString())
    }

    private fun updateFavoriteIcon(favouriteIcon: ImageButton, isFavourite: Boolean) {
        if (isFavourite) {
            favouriteIcon.setImageResource(R.drawable.baseline_favorite_24)
        } else {
            favouriteIcon.setImageResource(R.drawable.baseline_favorite_border_24)
        }
    }

    private fun gettingViewModelReady() {
        val viewModelFactory = HomeViewModelFactory(
            repository = HomeRepositoryImp(
                remoteDataSource = APIClient
            )
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    private fun getRandomLetter(): Char {
        return ('a'..'z').random()
    }

    override fun onClick(isFavourite: Boolean, recipe: Recipe) {
        lifecycleScope.launch {
            if (isFavourite) {
                AppDatabase.getDatabase(requireContext()).recipeDao().deleteRecipeWithMeal(recipe.meal!!)
            } else {
                AuthHelper.getUserID(requireContext())?.let { userId ->
                    val newRecipe = recipe.copy(favourite = true)
                    AppDatabase.getDatabase(requireContext()).recipeDao().insertRecipe(newRecipe)
                }
            }
        }
    }

    override fun onClick(position: Int, recipe: Recipe) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(recipe)
        findNavController().navigate(action)
    }

    private fun handleOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Custom back press logic if needed
                // For now, just call the default back press handling
                isEnabled = false
                requireActivity().onBackPressed()
            }
        })
    }
}
