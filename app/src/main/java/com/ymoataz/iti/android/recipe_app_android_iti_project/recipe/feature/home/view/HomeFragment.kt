package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.home.view
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ymoataz.iti.android.recipe_app_android_iti_project.R
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.core.common.AuthHelper
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.AppDatabase
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.local.entities.Recipe
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.common.adapters.RecipesRecyclerViewAdapter
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.home.repo.HomeRepositoryImp
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.home.viewModel.HomeViewModel
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.home.viewModel.HomeViewModelFactory
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network.data_sources.impl.RecipeRemoteDataSourceImpl
import kotlinx.coroutines.launch
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network.models.categories.CategoryX
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.search.view.SearchFragment

class HomeFragment : Fragment(), RecipesRecyclerViewAdapter.OnRecipeItemClickListener,
    RecipesRecyclerViewAdapter.OnFavouriteIconClickListener, CategoryAdapter.OnCategoryClickListener {

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: RecipesRecyclerViewAdapter
    private lateinit var rv: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var recycleViewTitle: TextView

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val mainCard = view.findViewById<View>(R.id.single_card)
        val cardImage = view.findViewById<ImageView>(R.id.recipeImageView)
        val cardTitle = view.findViewById<TextView>(R.id.recipeTitleTextView)
        val cardCategory = view.findViewById<TextView>(R.id.recipeCategoryTextView)
        val favouriteIcon = view.findViewById<ImageButton>(R.id.favoriteIcon)
        recycleViewTitle = view.findViewById<TextView>(R.id.recycle_view_title)

        rv = view.findViewById(R.id.home_recycle_view)
        rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv.adapter = RecipesRecyclerViewAdapter(emptyList(), view.context, this, this)
        rv.setHasFixedSize(true)
        adapter = rv.adapter as RecipesRecyclerViewAdapter

        categoriesRecyclerView = view.findViewById(R.id.categories_recycler_view)
        categoriesRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        categoryAdapter = CategoryAdapter(emptyList(), this)
        categoriesRecyclerView.adapter = categoryAdapter

        rv.adapter = adapter
        gettingViewModelReady()
//        viewModel.getRandomMeal()
        viewModel.randomMeal.observe(viewLifecycleOwner) { mealResponse ->
            if (mealResponse.meals.isEmpty()) {
                Glide.with(this)
                    .load(R.drawable.error)
                    .placeholder(R.drawable.loading)
                    .into(cardImage)
                cardTitle.text = "No Internet connection!"
                return@observe
            }

            val meal = mealResponse.meals[0]
            cardTitle.text = meal.strMeal
            Glide.with(this)
                .load(meal.strMealThumb)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(cardImage)

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

                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Remove from Favorites")
                            .setMessage("Are you sure you want to remove?")
                            .setPositiveButton("Yes") { dialog, _ ->
                                lifecycleScope.launch {
                                    AppDatabase.getDatabase(requireContext()).recipeDao().deleteRecipeWithMeal(meal)
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
                            AppDatabase.getDatabase(requireContext()).recipeDao().insertRecipe(recipe)
                            updateFavoriteIcon(favouriteIcon, !currentIsFavourite)

                        }
                    }
                }
            }
        }

        val randomChar = getRandomLetter()
//        viewModel.searchByFirstLetter(randomChar.toString())
        viewModel.searchedMeal.observe(viewLifecycleOwner) { searchResult ->
            val data = searchResult?.meals ?: emptyList()
            lifecycleScope.launch {
                val userId = AuthHelper.getUserID(requireContext())
                val recipes = userId?.let { AppDatabase.getDatabase(requireContext()).recipeDao().getAllRecipes(it) }
                val recipeList = data.map { meal ->
                    val isFavourite = recipes?.any { recipe -> recipe.meal?.idMeal == meal.idMeal } ?: false
                    Recipe(0, userId, meal, isFavourite)
                }

                adapter.updateData(recipeList)
                adapter.notifyDataSetChanged()
            }
        }

        mainCard.setOnClickListener {
            viewModel.randomMeal.value?.meals?.get(0)?.let { meal ->
                AuthHelper.getUserID(requireContext())?.let { userId ->
                    val recipe = Recipe(0, userId, meal, false)
                    val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(recipe)
                    findNavController().navigate(action)
                }
            }
        }

//        viewModel.getCategories()
        viewModel.categories.observe(viewLifecycleOwner) { categoryResponse ->
            categoryAdapter.updateData(categoryResponse.categories)
        }

        return view
    }

    private fun refreshScreen() {
        val randomChar = getRandomLetter()
//        viewModel.searchByFirstLetter(randomChar.toString())
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
    override fun onCategoryClick(category: CategoryX) {
//        val action = HomeFragmentDirections.actionHomeFragmentToMealsByCategoryFragment(category.strCategory )
//        findNavController().navigate(action)
        viewModel.getMealsByCategory(category.strCategory)
        recycleViewTitle.text = "${resources.getText(R.string.popular_now)}: ${category.strCategory}"
    }

}
