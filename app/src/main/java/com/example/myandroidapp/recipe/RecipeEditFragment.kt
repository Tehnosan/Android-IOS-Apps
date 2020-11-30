package com.example.myandroidapp.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.myandroidapp.R
import kotlinx.android.synthetic.main.fragment_recipe_edit.*

class RecipeEditFragment: Fragment() {
    companion object{
        const val RECIPE_ID = "RECIPE_ID"
    }

    private lateinit var viewModel: RecipeEditViewModel
    private var recipeId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if(it.containsKey(RECIPE_ID)){
                recipeId = it.getString(RECIPE_ID)
            }
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipe_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recipe_name.setText(recipeId)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupViewModel()

        fab.setOnClickListener {
            viewModel.saveOrUpdateItem(recipe_name.text.toString(), recipe_difficulty.text.toString(), recipe_time.text.toString())
        }
    }

    private fun setupViewModel(){
        viewModel = ViewModelProvider(this).get(RecipeEditViewModel::class.java)
        viewModel.recipe.observe(viewLifecycleOwner) { recipe ->
            recipe_name.setText(recipe.name)
        }
        viewModel.recipe.observe(viewLifecycleOwner) { recipe ->
            recipe_difficulty.setText(recipe.difficulty)
        }
        viewModel.recipe.observe(viewLifecycleOwner) { recipe ->
            recipe_time.setText(recipe.time)
        }

        viewModel.fetching.observe(viewLifecycleOwner){fetching ->
            progress.visibility = if(fetching) View.VISIBLE else View.GONE
        }
        viewModel.fetchingError.observe(viewLifecycleOwner) { exception ->
            if (exception != null) {
                val message = "Fetching exception ${exception.message}"
                val parentActivity = activity?.parent
                if (parentActivity != null) {
                    Toast.makeText(parentActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.completed.observe(viewLifecycleOwner, Observer { completed ->
            if (completed) {
                findNavController().navigateUp()
            }
        })

        val id = recipeId
        if(id != null){
            viewModel.loadItem(id)
        }
    }
}