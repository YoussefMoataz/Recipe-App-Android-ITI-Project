package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.search.view

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
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ymoataz.iti.android.recipe_app_android_iti_project.R
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.adapter.MyAdapter
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.adapter.Recipe
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network.APIClient
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.search.repo.SearchRepositoryImp
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.search.viewModel.SearchViewModel
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.search.viewModel.SearchViewModelFactory


class SearchFragment : Fragment(), MyAdapter.OnRecipeItemClickListener {
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
            var recipe = data.map { Recipe(1, it, true) }

            if (searchView.query.isEmpty() || recipe.isEmpty()) {
                if(recipe.isEmpty())
                    notFoundTextView.visibility = View.VISIBLE
                recipe = emptyList()
            }
            if(recipe.isNotEmpty())
                notFoundTextView.visibility = View.GONE


            rv.adapter = MyAdapter(recipe, view.context, this)

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

}
