package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.common.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.ymoataz.iti.android.recipe_app_android_iti_project.R
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.local.entities.Recipe

class RecipesRecyclerViewAdapter(
    private var data: List<Recipe>,
    private val context: Context,
    private val onRecipeItemClickListener: OnRecipeItemClickListener,
    private val onFavouriteIconClickListener: OnFavouriteIconClickListener
) : RecyclerView.Adapter<RecipesRecyclerViewAdapter.MyViewHolder>() {

    class MyViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        val cardView = row.findViewById<CardView>(R.id.single_card)
        val image = row.findViewById<ShapeableImageView>(R.id.recipeImageView)
        val title = row.findViewById<TextView>(R.id.recipeTitleTextView)
        val favouriteIcon = row.findViewById<ImageButton>(R.id.favoriteIcon)
        val category = row.findViewById<TextView>(R.id.recipeCategoryTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.new_card, parent, false)
        return MyViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val recipe = data[position]

        holder.cardView.setOnClickListener {
            onRecipeItemClickListener.onClick(position, recipe)
        }

        holder.title.text = recipe.meal?.strMeal
        holder.category.text = recipe.meal?.strCategory

        Glide.with(context)
            .load(recipe.meal?.strMealThumb)
            .placeholder(R.drawable.loading)
            .error(R.drawable.error)
            .into(holder.image)

        updateFavoriteIcon(holder.favouriteIcon, recipe.favourite!!)

        holder.favouriteIcon.setOnClickListener {
            onFavouriteIconClickListener.onClick(recipe.favourite!!, recipe)
            recipe.favourite = !recipe.favourite!!
            updateFavoriteIcon(holder.favouriteIcon, recipe.favourite!!)
        }
    }

    private fun updateFavoriteIcon(favouriteIcon: ImageButton, isFavourite: Boolean) {
        if (isFavourite) {
            favouriteIcon.setImageResource(R.drawable.baseline_favorite_24)
        } else {
            favouriteIcon.setImageResource(R.drawable.baseline_favorite_border_24)
        }
    }

    fun updateData(newData: List<Recipe>) {
        data = newData
        notifyDataSetChanged()
    }

    interface OnRecipeItemClickListener {
        fun onClick(position: Int, recipe: Recipe)
    }

    interface OnFavouriteIconClickListener {
        fun onClick(isFavourite: Boolean, recipe: Recipe)
    }
}
