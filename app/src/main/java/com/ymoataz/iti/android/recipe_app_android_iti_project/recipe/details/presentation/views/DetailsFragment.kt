package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.details.presentation.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.colormoon.readmoretextview.ReadMoreTextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.ymoataz.iti.android.recipe_app_android_iti_project.R
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.AuthHelper
import com.ymoataz.iti.android.recipe_app_android_iti_project.database.AppDatabase
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.details.helpers.IngredientsHelper
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.details.helpers.YoutubeHelper
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.details.presentation.adapters.DetailsIngredientsAdapter
import kotlinx.coroutines.launch

class DetailsFragment : Fragment() {

    private val args: DetailsFragmentArgs by navArgs()
    lateinit var youTubePlayerView: YouTubePlayerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_details, container, false)

        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility = View.GONE

        val nameTextView = view.findViewById<TextView>(R.id.recipe_name_text_view)
        val areaTextView = view.findViewById<TextView>(R.id.recipe_area_text_view)
        val categoryTextView = view.findViewById<TextView>(R.id.recipe_category_text_view)
        val instructionsTextView = view.findViewById<ReadMoreTextView>(R.id.recipe_instructions_text_view)
        val ingredientsRecyclerView = view.findViewById<RecyclerView>(R.id.recipe_ingredients_recycler_view)
        val thumbImageView = view.findViewById<ImageView>(R.id.recipe_thumb_image_view)
        val favouriteIcon = view.findViewById<ImageButton>(R.id.favoriteIcon)
        youTubePlayerView = view.findViewById(R.id.recipe_youtube_player_view)
        var myYouTubePlayer: YouTubePlayer? = null

        val recipe = args.recipe

        if (recipe?.meal != null) {
            Glide.with(this).load(recipe.meal.strMealThumb).into(thumbImageView)
            nameTextView.text = recipe.meal.strMeal
            areaTextView.text = recipe.meal.strArea
            categoryTextView.text = recipe.meal.strCategory
            instructionsTextView.text = recipe.meal.strInstructions

            val ingredientsAdapter = DetailsIngredientsAdapter(
                requireActivity(),
                IngredientsHelper.getNonEmptyIngredientsAndMeasures(recipe.meal)
            )

            ingredientsRecyclerView.adapter = ingredientsAdapter
            ingredientsRecyclerView.layoutManager = LinearLayoutManager(activity)

            updateFavoriteIcon(favouriteIcon, recipe.favourite ?: false)
        }

        val youTubePlayerListener = object : AbstractYouTubePlayerListener(){
            override fun onReady(youTubePlayer: YouTubePlayer) {
                YoutubeHelper.getVideoId(recipe?.meal?.strYoutube ?: "").also {
                    if (it.isEmpty()) return

                    youTubePlayer.cueVideo(it, 0f)
                    myYouTubePlayer = youTubePlayer
                }

            }
        }

        favouriteIcon.setOnClickListener {
            lifecycleScope.launch {
                recipe?.favourite?.let { isFavourite ->
                    if (isFavourite) {
                        AppDatabase.getDatabase(requireContext()).recipeDao()
                            .deleteRecipeWithMeal(recipe.meal!!)
                    } else {
                        AuthHelper.getUserID(requireContext())?.let { userId ->
                            val newRecipe = recipe.copy(favourite = true)
                            AppDatabase.getDatabase(requireContext()).recipeDao()
                                .insertRecipe(newRecipe)
                        }
                    }
                    recipe.favourite = !recipe.favourite!!
                    updateFavoriteIcon(favouriteIcon, recipe.favourite!!)
                }
            }
        }

        lifecycle.addObserver(youTubePlayerView)
        youTubePlayerView.addYouTubePlayerListener(youTubePlayerListener)

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility = View.VISIBLE
        youTubePlayerView.release();
    }

    private fun updateFavoriteIcon(favouriteIcon: ImageButton, isFavourite: Boolean) {
        if (isFavourite) {
            favouriteIcon.setImageResource(R.drawable.baseline_favorite_24)
        } else {
            favouriteIcon.setImageResource(R.drawable.baseline_favorite_border_24)
        }
    }
}