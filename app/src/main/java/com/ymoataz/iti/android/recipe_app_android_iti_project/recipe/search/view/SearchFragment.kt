package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.search.view

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ymoataz.iti.android.recipe_app_android_iti_project.R
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.AuthHelper
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.AppDatabase
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.adapter.MyAdapter
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.Recipe
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network.APIClient
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.search.repo.SearchRepositoryImp
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.search.viewModel.SearchViewModel
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.search.viewModel.SearchViewModelFactory
import kotlinx.coroutines.launch


class SearchFragment : Fragment(), MyAdapter.OnRecipeItemClickListener,
    MyAdapter.OnFavouriteIconClickListener {
    private lateinit var viewModel : SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val notFoundTextView = view.findViewById<TextView>(R.id.notFoundTextView)
        val searchView = view.findViewById<SearchView>(R.id.searchView)

        val searchViewModelFactory = SearchViewModelFactory(SearchRepositoryImp(APIClient))
        viewModel = ViewModelProvider(this, searchViewModelFactory)[SearchViewModel::class.java]
        val rv = view.findViewById<RecyclerView>(R.id.searchRecycleView)
        rv.layoutManager = LinearLayoutManager(view.context)

        viewModel.searchResult.observe(viewLifecycleOwner) { searchResult ->
            val data = searchResult?.meals ?: emptyList()
            lifecycleScope.launch {
                val recipes = AuthHelper.getUserID(requireContext())?.let { it1 ->
                    AppDatabase.getDatabase(requireContext()).recipeDao().getAllRecipes(it1)
                }

                var recipe = data.map { meal ->
                    val isFavourite = recipes?.any { recipe -> recipe.meal?.idMeal == meal.idMeal }
                    Recipe(
                        0,
                        AuthHelper.getUserID(requireContext()),
                        meal,
                        isFavourite
                    )
                }

                if (searchView.query.isEmpty() || recipe.isEmpty()) {
                    if (recipe.isEmpty())
                        notFoundTextView.visibility = View.VISIBLE
                    recipe = emptyList()
                }

                rv.adapter =
                    MyAdapter(recipe, view.context, object : MyAdapter.OnRecipeItemClickListener {
                        override fun onClick(position: Int, recipe: Recipe) {
                            val action =
                                SearchFragmentDirections.actionSearchFragmentToDetailsFragment(
                                    recipe
                                )
                            findNavController().navigate(action)
                        }
                    }, this@SearchFragment)
                if (recipe.isNotEmpty())
                    notFoundTextView.visibility = View.GONE


                rv.adapter = MyAdapter(recipe, view.context, this@SearchFragment, this@SearchFragment)

            }
        }
        focusAndOpenKeyboard(searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.search(newText)
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
        if (isFavourite){
            lifecycleScope.launch {
                recipe.meal?.let {
                    AppDatabase.getDatabase(requireContext()).recipeDao().deleteRecipeWithMeal(
                        it
                    )
                }
            }
        }
        else
        {
            lifecycleScope.launch {
                AppDatabase.getDatabase(requireContext()).recipeDao().insertRecipe(recipe)
            }
        }
    }

}
