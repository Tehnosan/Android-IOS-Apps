package com.example.myandroidapp.data.remote

import com.example.myandroidapp.data.Recipe
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

object RecipeApi {
    private const val URL = "http://192.168.1.3:5000/api/v1/"

    interface Service{
        @GET("recipes")
        suspend fun find(): List<Recipe>

        @GET("recipes/{id}")
        suspend fun findOne(@Path("id") id: String): Recipe

        @Headers("Content-Type: application/json")
        @POST("recipe")
        suspend fun create(@Body recipe: Recipe): Recipe

        @Headers("Content-Type: application/json")
        @PUT("recipe/{id}")
        suspend fun update(@Path("id") recipeId: String, @Body recipe: Recipe): Recipe
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES) // connect timeout
            .writeTimeout(5, TimeUnit.MINUTES) // write timeout
            .readTimeout(5, TimeUnit.MINUTES)
            .build()

    private var gson = GsonBuilder().setLenient().create()

    val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    val service: Service = retrofit.create(Service::class.java)
}