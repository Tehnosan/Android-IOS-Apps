package com.example.myandroidapp.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myandroidapp.data.Recipe
import com.example.myandroidapp.data.RecipeRepo
import kotlinx.coroutines.launch
import java.lang.Exception

class RecipeEditViewModel : ViewModel(){
    private val mutableRecipe = MutableLiveData<Recipe>().apply { value = Recipe("", "", "", "") }
    private val mutableFetching = MutableLiveData<Boolean>().apply { value = false }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val recipe: LiveData<Recipe> = mutableRecipe
    val fetching: LiveData<Boolean> = mutableFetching
    val fetchingError: LiveData<Exception> = mutableException
    val completed: LiveData<Boolean> = mutableCompleted

    fun loadItem(recipeId: String) {
        viewModelScope.launch {
            mutableFetching.value = true
            mutableException.value = null
            try {
                mutableRecipe.value = RecipeRepo.load(recipeId)
                mutableFetching.value = false
            } catch (e: Exception) {
                mutableException.value = e
                mutableFetching.value = false
            }
        }
    }

    fun saveOrUpdateItem(name: String, difficulty: String, time: String) {
        viewModelScope.launch {
            val recipe = mutableRecipe.value ?: return@launch
            recipe.name = name
            recipe.difficulty = difficulty
            recipe.time = time
            mutableFetching.value = true
            mutableException.value = null
            try {
                if (recipe.id.isNotEmpty()) {
                     mutableRecipe.value = RecipeRepo.update(recipe)
                } else {
                    val newId = RecipeRepo.size();

                    if(newId != null){
                        recipe.id = (newId + 1).toString()
                    }
                    else{
                        recipe.id = "1"
                    }
                    mutableRecipe.value = RecipeRepo.save(recipe)
                }
                mutableCompleted.value = true
                mutableFetching.value = false
            } catch (e: Exception) {
                mutableException.value = e
                mutableFetching.value = false
            }
        }
    }
}