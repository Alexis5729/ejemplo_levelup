package com.example.ejemplo_level_up.data.remote

import com.example.ejemplo_level_up.data.model.Post
import retrofit2.http.GET

interface ApiService {
    @GET("posts")
    suspend fun getPosts(): List<Post>
}