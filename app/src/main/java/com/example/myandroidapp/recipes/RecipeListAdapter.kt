package com.example.myandroidapp.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.myandroidapp.data.Recipe
import com.example.myandroidapp.R
import com.example.myandroidapp.recipe.RecipeEditFragment
import kotlinx.android.synthetic.main.view_recipe.view.*

class RecipeListAdapter(
        private val fragment: Fragment
) : RecyclerView.Adapter<RecipeListAdapter.ViewHolder>() {

    var recipes = emptyList<Recipe>()
        set(value) {
            field = value
            notifyDataSetChanged();
        }

    private var onRecipeClick: View.OnClickListener;

    init {
        onRecipeClick = View.OnClickListener { view ->
            val recipe = view.tag as Recipe
            fragment.findNavController().navigate(R.id.RecipeEditFragment, Bundle().apply {
                putString(RecipeEditFragment.RECIPE_ID, recipe.id)
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_recipe, parent, false)
        //on create view holder
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //on bind view holder
        val recipe = recipes[position]
        holder.itemView.tag = recipe;
        holder.textView.text = recipe.name
        holder.itemView.setOnClickListener(onRecipeClick)
    }

    override fun getItemCount() = recipes.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.text
    }
}