package com.example.ejemplo_level_up.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.ejemplo_level_up.data.database.GameDatabase
import com.example.ejemplo_level_up.data.repository.GameRepository

class DetailViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = GameRepository(GameDatabase.getInstance(app).gameDao())
    fun game(id: String) = repo.gameById(id)
}