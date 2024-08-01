package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

class MealsByCategoryFragment : Fragment(), MyAdapter.OnRecipeItemClickListener,
    MyAdapter.OnFavouriteIconClickListener {

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: MyAdapter
    private lateinit var rv: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_meals_by_category, container, false)
        rv = view.findViewById(R.id.meals_recycler_view)
        rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv.setHasFixedSize(true)

        adapter = MyAdapter(emptyList(), requireContext(), this, this)
        rv.adapter = adapter

        gettingViewModelReady()

        val categoryName = arguments?.getString("categoryName") ?: ""
        viewModel.getMealsByCategory(categoryName)
        viewModel.mealsByCategory.observe(viewLifecycleOwner) { searchResult ->
            val data = searchResult?.meals ?: emptyList()
            lifecycleScope.launch {
                val userId = AuthHelper.getUserID(requireContext())
                val recipes = userId?.let { AppDatabase.getDatabase(requireContext()).recipeDao().getAllRecipes(it) }
                val recipeList = data.map { meal ->
                    val isFavourite = recipes?.any { recipe -> recipe.meal?.idMeal == meal.idMeal } ?: false
                    Recipe(0, userId, meal, isFavourite)
                }

                if (recipeList.isEmpty()) {
                    refreshScreen(categoryName)
                } else {
                    adapter.updateData(recipeList)
                    adapter.notifyDataSetChanged()
                }
            }
        }
        return view
    }

    private fun gettingViewModelReady() {
        val viewModelFactory = HomeViewModelFactory(
            repository = HomeRepositoryImp(
                remoteDataSource = APIClient
            )
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    private fun refreshScreen(categoryName: String) {
        viewModel.getMealsByCategory(categoryName)
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
        val action = MealsByCategoryFragmentDirections.actionMealsByCategoryFragmentToDetailsFragment(recipe)
        findNavController().navigate(action)
    }
}

