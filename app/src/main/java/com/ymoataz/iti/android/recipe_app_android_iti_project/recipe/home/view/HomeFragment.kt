package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.ymoataz.iti.android.recipe_app_android_iti_project.R
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.home.repo.HomeRepositoryImp
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.home.viewModel.HomeViewModel
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.home.viewModel.HomeViewModelFactory
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network.APIClient
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network.RemoteDataSource


class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val cardImage= view.findViewById<ImageView>(R.id.recipeImageView)
        val cardTitle= view.findViewById<TextView>(R.id.recipeTitleTextView)
        val cardCategory= view.findViewById<TextView>(R.id.recipeCategoryTextView)
        gettingViewModelReady()
        viewModel.getRandomMeal()
        viewModel.randomMeal.observe(viewLifecycleOwner) {
            cardTitle.text = it.meals[0].strMeal
            cardCategory.text = it.meals[0].strCategory
            Glide.with(this).load(it.meals[0].strMealThumb).into(cardImage)
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

}