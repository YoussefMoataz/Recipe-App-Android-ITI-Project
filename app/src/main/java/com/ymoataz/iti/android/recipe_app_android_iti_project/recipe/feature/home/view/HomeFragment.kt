package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.home.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ymoataz.iti.android.recipe_app_android_iti_project.R
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.core.common.AuthHelper
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.AppDatabase
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.common.adapters.RecipesRecyclerViewAdapter
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.common.connectivity.ConnectivityObserver
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.local.entities.Recipe
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network.data_sources.impl.RecipeRemoteDataSourceImpl
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network.models.categories.CategoryX
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.home.repo.HomeRepositoryImp
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.home.viewModel.HomeViewModel
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.home.viewModel.HomeViewModelFactory
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), RecipesRecyclerViewAdapter.OnRecipeItemClickListener,
    RecipesRecyclerViewAdapter.OnFavouriteIconClickListener,
    CategoryAdapter.OnCategoryClickListener {

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: RecipesRecyclerViewAdapter
    private lateinit var rv: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var recycleViewTitle: TextView
    private lateinit var noInternetAnimation: LottieAnimationView
    private lateinit var loadingAnimation: LottieAnimationView
    private lateinit var scrollView: NestedScrollView

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        var categoryTitle = view.findViewById<TextView>(R.id.categories_title)
        val mainCard = view.findViewById<View>(R.id.single_card)
        val cardImage = view.findViewById<ImageView>(R.id.recipeImageView)
        val cardTitle = view.findViewById<TextView>(R.id.recipeTitleTextView)
        val cardCategory = view.findViewById<TextView>(R.id.recipeCategoryTextView)
        val favouriteIcon = view.findViewById<ImageButton>(R.id.favoriteIcon)
        recycleViewTitle = view.findViewById<TextView>(R.id.recycle_view_title)
        noInternetAnimation = view.findViewById(R.id.no_internet_animation)
        loadingAnimation = view.findViewById(R.id.loading_food_animation)
        scrollView = view.findViewById(R.id.home_scroll_view)
        rv = view.findViewById(R.id.home_recycle_view)
        rv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv.adapter = RecipesRecyclerViewAdapter(emptyList(), view.context, this, this)
        rv.setHasFixedSize(true)
        adapter = rv.adapter as RecipesRecyclerViewAdapter

        categoriesRecyclerView = view.findViewById(R.id.categories_recycler_view)
        categoriesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        categoryAdapter = CategoryAdapter(emptyList(), this)
        categoriesRecyclerView.adapter = categoryAdapter

        rv.adapter = adapter

        gettingViewModelReady()

        mainCard.setOnClickListener {
            viewModel.randomMeal.value?.meals?.get(0)?.let { meal ->
                AuthHelper.getUserID(requireContext())?.let { userId ->
                    lifecycleScope.launch {
                        val isFavourite  = AppDatabase.getDatabase(requireContext()).recipeDao().getAllRecipes(userId)
                            ?.any { recipe ->
                                recipe.meal?.idMeal == meal.idMeal
                            }
                        val recipe = Recipe(0, userId, meal, isFavourite)
                        val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(recipe)
                        findNavController().navigate(action)
                    }


                }
            }
        }

        favouriteIcon.setOnClickListener {
            lifecycleScope.launch {
                viewModel.randomMeal.value?.meals?.get(0)?.let { meal ->
                    val userId = AuthHelper.getUserID(requireContext())
                    val currentIsFavourite = userId?.let { id ->
                        AppDatabase.getDatabase(requireContext()).recipeDao().getAllRecipes(id)
                            ?.any { recipe ->
                                recipe.meal?.idMeal == meal.idMeal
                            } ?: false
                    } ?: false

                    if (currentIsFavourite) {

                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Remove from Favorites")
                            .setMessage("Are you sure you want to remove?")
                            .setPositiveButton("Yes") { dialog, _ ->
                                lifecycleScope.launch {
                                    AppDatabase.getDatabase(requireContext()).recipeDao()
                                        .deleteRecipeWithMeal(meal)
                                    updateFavoriteIcon(favouriteIcon, !currentIsFavourite)

                                }
                                dialog.dismiss()
                            }
                            .setNegativeButton("No") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .create()
                            .show()

                    } else {
                        userId?.let { id ->
                            val recipe = Recipe(0, id, meal, true)
                            AppDatabase.getDatabase(requireContext()).recipeDao()
                                .insertRecipe(recipe)
                            updateFavoriteIcon(favouriteIcon, !currentIsFavourite)

                        }
                    }
                }
            }
        }

        viewModel.randomMeal.observe(viewLifecycleOwner) { mealResponse ->

            if (mealResponse.meals.isEmpty()) {
                Glide.with(this)
                    .load(R.drawable.loading)
                    .into(cardImage)
//                cardTitle.text = "No Internet connection!"
                return@observe
            }

            val meal = mealResponse.meals.random()
            cardTitle.text = meal.strMeal
            Glide.with(this)
                .load(meal.strMealThumb)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(cardImage)

            lifecycleScope.launch {
                val userId = AuthHelper.getUserID(requireContext())
                val recipes = userId?.let {
                    AppDatabase.getDatabase(requireContext()).recipeDao().getAllRecipes(it)
                }
                val isFavourite = recipes?.any { recipe ->
                    recipe.meal?.idMeal == meal.idMeal
                } ?: false
                updateFavoriteIcon(favouriteIcon, isFavourite)
            }
        }

        viewModel.categories.observe(viewLifecycleOwner) { categoryResponse ->
            if (categoryResponse.categories.isEmpty()) {
                categoryTitle = view.findViewById<TextView>(R.id.categories_title)
                categoryTitle.visibility = View.GONE
                categoriesRecyclerView.visibility = View.GONE
                return@observe
            }

            categoriesRecyclerView.visibility = View.VISIBLE
            categoryTitle.visibility = View.VISIBLE
            categoryAdapter.updateData(categoryResponse.categories)
        }

        viewModel.searchedMeal.observe(viewLifecycleOwner) { searchResult ->
            val data = searchResult?.meals ?: emptyList()

            if (data.isEmpty() && viewModel.connectionStatus.value != ConnectivityObserver.Status.Available) {
                showNoInternetAnimation()
                return@observe
            } else {
                recycleViewTitle.text = resources.getText(R.string.popular_now)
                hideNoInternetAnimation()
            }

            lifecycleScope.launch {

                val userId = AuthHelper.getUserID(requireContext())
                val recipes = userId?.let {
                    AppDatabase.getDatabase(requireContext()).recipeDao().getAllRecipes(it)
                }
                val recipeList = data.map { meal ->
                    val isFavourite = recipes?.any { recipe ->
                        recipe.meal?.idMeal == meal.idMeal
                    } ?: false
                    Recipe(0, userId, meal, isFavourite)
                }

                adapter.updateData(recipeList)
                adapter.notifyDataSetChanged()
            }
        }

        return view
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
                recipeRemoteDataSource = RecipeRemoteDataSourceImpl
            ),
            context = requireContext()
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    private fun showNoInternetAnimation() {
        noInternetAnimation.visibility = View.VISIBLE
        scrollView.visibility = View.GONE
        loadingAnimation.visibility = View.GONE
    }

    private fun hideNoInternetAnimation() {
        loadingAnimation.visibility = View.GONE
        noInternetAnimation.visibility = View.GONE
        scrollView.visibility = View.VISIBLE
    }

    override fun onClick(isFavourite: Boolean, recipe: Recipe) {
        lifecycleScope.launch {
            if (isFavourite) {
                AppDatabase.getDatabase(requireContext()).recipeDao()
                    .deleteRecipeWithMeal(recipe.meal!!)
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

    override fun onCategoryClick(category: CategoryX) {
//        val action = HomeFragmentDirections.actionHomeFragmentToMealsByCategoryFragment(category.strCategory )
//        findNavController().navigate(action)
        viewModel.getMealsByCategory(category.strCategory)
        recycleViewTitle.text =
            "${resources.getText(R.string.popular_now)}: ${category.strCategory}"
    }

}