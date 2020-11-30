package com.example.myandroidapp.data

import android.util.Log
import com.example.myandroidapp.data.remote.RecipeApi

object RecipeRepo {
    private var cachedRecipes: MutableList<Recipe>? = null;

    suspend fun loadAll(): List<Recipe>{
        if(cachedRecipes != null){
            return cachedRecipes as List<Recipe>;
        }

        cachedRecipes = mutableListOf()
        val recipes = RecipeApi.service.find()
        //val recipes = arrayListOf(Recipe("1", "Recipe1", "diff", "15"))
        cachedRecipes?.addAll(recipes)

        return cachedRecipes as List<Recipe>
    }

    suspend fun load(recipeId: String): Recipe {
        val recipe = cachedRecipes?.find { it.id == recipeId }
        //val recipe = Recipe("2", "Recipe2", "diff", "15")
        if (recipe != null) {
            return recipe
        }
        return RecipeApi.service.findOne(recipeId)
    }

    suspend fun save(recipe: Recipe): Recipe {
        val createdRecipe = RecipeApi.service.create(recipe)
        cachedRecipes?.add(createdRecipe)
        return createdRecipe
    }

    suspend fun update(recipe: Recipe): Recipe {
        val updatedRecipe = RecipeApi.service.update(recipe.id, recipe)
        val index = cachedRecipes?.indexOfFirst { it.id == recipe.id }
        if (index != null) {
            cachedRecipes?.set(index, updatedRecipe)
        }
        return updatedRecipe
    }

    suspend fun size(): Int?{
        return cachedRecipes?.size;
    }
}