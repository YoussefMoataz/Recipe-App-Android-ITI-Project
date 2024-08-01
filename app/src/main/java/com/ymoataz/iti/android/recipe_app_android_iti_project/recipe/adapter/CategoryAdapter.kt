package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ymoataz.iti.android.recipe_app_android_iti_project.R
import com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.network.dto.CategoryX
class CategoryAdapter(
    private var categories: List<CategoryX>,
    private val onCategoryClickListener: OnCategoryClickListener
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    interface OnCategoryClickListener {
        fun onCategoryClick(category: CategoryX)
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryImage: ImageView = itemView.findViewById(R.id.categoryImageView)
        val categoryName: TextView = itemView.findViewById(R.id.categoryNameTextView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.categoryName.text = category.strCategory
        Glide.with(holder.itemView.context).load(category.strCategoryThumb).into(holder.categoryImage)

        holder.itemView.setOnClickListener {
            onCategoryClickListener.onCategoryClick(category)
        }
    }

    override fun getItemCount() = categories.size

    fun updateData(newCategories: List<CategoryX>) {
        categories = newCategories
        notifyDataSetChanged()
    }
}
