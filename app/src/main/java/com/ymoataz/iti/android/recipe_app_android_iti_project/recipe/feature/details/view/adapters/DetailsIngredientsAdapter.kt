package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.details.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ymoataz.iti.android.recipe_app_android_iti_project.R
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.feature.details.domain.Ingredient

class DetailsIngredientsAdapter(val context: Context, val ingredients: List<Ingredient>):
    RecyclerView.Adapter<DetailsIngredientsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val ingredientTextView = itemView.findViewById<TextView>(R.id.ingredient_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.ingredient_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = ingredients.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ingredientTextView.text = ingredients[position].ingredient + " - " + ingredients[position].measure
    }
}