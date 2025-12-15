package com.example.ejemplo_level_up.data.repository

import com.example.ejemplo_level_up.data.model.Post
import com.example.ejemplo_level_up.data.remote.RetrofitInstance

class PostRepository {
    suspend fun getPosts(): List<Post> = RetrofitInstance.api.getPosts()
}