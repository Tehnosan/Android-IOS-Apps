package com.example.myandroidapp.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.myandroidapp.R
import kotlinx.android.synthetic.main.fragment_recipe_list.*

class RecipeListFragment : Fragment() {
    private lateinit var recipeListAdapter: RecipeListAdapter
    private lateinit var recipesModel: RecipeListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //on create
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipe_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //on activity created
        setupItemList()
        fab.setOnClickListener {
            //add new item
            findNavController().navigate(R.id.RecipeEditFragment)
        }
    }

    private fun setupItemList() {
        recipeListAdapter = RecipeListAdapter(this)
        recipe_list.adapter = recipeListAdapter
        recipesModel = ViewModelProvider(this).get(RecipeListViewModel::class.java)
        recipesModel.recipes.observe(viewLifecycleOwner) { recipes ->
            //update items
            recipeListAdapter.recipes = recipes
        }
        recipesModel.loading.observe(viewLifecycleOwner) { loading ->
            //update loading
            progress.visibility = if (loading) View.VISIBLE else View.GONE
        }
        recipesModel.loadingError.observe(viewLifecycleOwner) { exception ->
            if (exception != null) {
                //update loading error
                val message = "Loading exception ${exception.message}"
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        }
        recipesModel.loadItems()
    }

    override fun onDestroy() {
        super.onDestroy()
        //on destroy
    }
}