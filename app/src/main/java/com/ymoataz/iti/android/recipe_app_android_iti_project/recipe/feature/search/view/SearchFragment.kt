package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.search.view

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.ymoataz.iti.android.recipe_app_android_iti_project.R
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.core.common.AuthHelper
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.AppDatabase
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.common.adapters.RecipesRecyclerViewAdapter
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.local.entities.Recipe
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.network.data_sources.impl.RecipeRemoteDataSourceImpl
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.search.repo.SearchRepositoryImp
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.search.viewModel.SearchViewModel
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.search.viewModel.SearchViewModelFactory
import kotlinx.coroutines.launch


class SearchFragment : Fragment(), RecipesRecyclerViewAdapter.OnRecipeItemClickListener,
    RecipesRecyclerViewAdapter.OnFavouriteIconClickListener {

    private lateinit var viewModel: SearchViewModel
    private lateinit var adapter: RecipesRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val notFoundAnimationView = view.findViewById<LottieAnimationView>(R.id.noResultsAnimationView)
        val searchView = view.findViewById<SearchView>(R.id.searchView)
        val rv = view.findViewById<RecyclerView>(R.id.searchRecycleView)

        val searchViewModelFactory = SearchViewModelFactory(SearchRepositoryImp(RecipeRemoteDataSourceImpl), requireContext())
        viewModel = ViewModelProvider(this, searchViewModelFactory)[SearchViewModel::class.java]

        rv.layoutManager = LinearLayoutManager(view.context)
        adapter = RecipesRecyclerViewAdapter(emptyList(), view.context, this, this)
        rv.adapter = adapter

        viewModel.searchResult.observe(viewLifecycleOwner) { searchResult ->
            val data = searchResult?.meals ?: emptyList()

            lifecycleScope.launch {
                val recipes = AuthHelper.getUserID(requireContext())?.let {
                    AppDatabase.getDatabase(requireContext()).recipeDao().getAllRecipes(it)
                }

                var recipeList = data.map { meal ->
                    val isFavourite = recipes?.any { recipe -> recipe.meal?.idMeal == meal.idMeal } ?: false
                    Recipe(0, AuthHelper.getUserID(requireContext()), meal, isFavourite)
                }

                if (searchView.query.isEmpty()) {
                    recipeList = emptyList()
                    notFoundAnimationView.visibility = View.GONE
                    notFoundAnimationView.pauseAnimation()
                }
                else if(recipeList.isEmpty())
                {
                    notFoundAnimationView.visibility = View.VISIBLE
                    notFoundAnimationView.playAnimation()
                    recipeList = emptyList()
                }
                else {
                    notFoundAnimationView.visibility = View.GONE
                    notFoundAnimationView.pauseAnimation()
                }

                adapter.updateData(recipeList)
            }
        }

        focusAndOpenKeyboard(searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.search(it)
                }
                return true
            }
        })

        return view
    }

    override fun onClick(position: Int, recipe: Recipe) {
        val action = SearchFragmentDirections.actionSearchFragmentToDetailsFragment(recipe)
        findNavController().navigate(action)
    }

    private fun focusAndOpenKeyboard(searchView: SearchView) {
        Handler(Looper.getMainLooper()).postDelayed({
            searchView.requestFocus()
            val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT)
        }, 200)
    }

    override fun onClick(isFavourite: Boolean, recipe: Recipe) {
        lifecycleScope.launch {
            if (isFavourite) {
                recipe.meal?.let {
                    AppDatabase.getDatabase(requireContext()).recipeDao().deleteRecipeWithMeal(it)
                }
            } else {
                AppDatabase.getDatabase(requireContext()).recipeDao().insertRecipe(recipe)
            }
        }
    }

}