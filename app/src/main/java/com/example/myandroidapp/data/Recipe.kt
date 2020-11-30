package com.example.myandroidapp.data

data class Recipe(
    var id: String,
    var name: String,
    var difficulty: String,
    var time: String
) {
    override fun toString(): String {
        return "Recipe(id=$id, name='$name', difficulty='$difficulty', time=$time)"
    }
}