package com.example.myandroidapp.recipes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myandroidapp.data.Recipe
import com.example.myandroidapp.data.RecipeRepo
import kotlinx.coroutines.launch

class RecipeListViewModel : ViewModel() {
    private val mutableRecipes = MutableLiveData<List<Recipe>>().apply { value = emptyList() }
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val recipes: LiveData<List<Recipe>> = mutableRecipes
    val loading: LiveData<Boolean> = mutableLoading
    val loadingError: LiveData<Exception> = mutableException

    fun createRecipe(position: Int): Unit {
        val list = mutableListOf<Recipe>()
        list.addAll(mutableRecipes.value!!)
        list.add(Recipe(position.toString(), "Recipe " + position, "D", "T"))
        mutableRecipes.value = list
    }

    fun loadItems() {
        viewModelScope.launch {
            mutableLoading.value = true
            mutableException.value = null
            try {
                mutableRecipes.value = RecipeRepo.loadAll()
                mutableLoading.value = false
            } catch (e: Exception) {
                mutableException.value = e
                mutableLoading.value = false
            }
        }
    }
}