package com.ymoataz.iti.android.recipe_app_android_iti_project.about_us.adapter

import CircleTransform
import Person
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.ymoataz.iti.android.recipe_app_android_iti_project.R

class PersonAdapter(private val personList: List<Person>) : RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {

    inner class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val githubTextView: TextView = itemView.findViewById(R.id.githubTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.about_card, parent, false)
        return PersonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = personList[position]
        holder.nameTextView.text = person.name
        holder.githubTextView.text = person.githubUrl

        // Load circular image with Picasso
        Picasso.get().load(person.imageResId).resize(80, 80).transform(CircleTransform()).into(holder.imageView)

        // Animate the card
        holder.itemView.alpha = 0f
        holder.itemView.translationY = 100f
        holder.itemView.animate().alpha(1f).translationY(0f).setDuration(500).start()
    }

    override fun getItemCount() = personList.size
}

