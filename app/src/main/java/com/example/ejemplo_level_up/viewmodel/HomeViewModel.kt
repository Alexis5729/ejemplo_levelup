package com.example.ejemplo_level_up.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ejemplo_level_up.data.database.GameDatabase
import com.example.ejemplo_level_up.data.model.Game
import com.example.ejemplo_level_up.data.repository.GameRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = GameRepository(GameDatabase.getInstance(app).gameDao())

    val games = repo.allGames().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun seed() = viewModelScope.launch {
        val seed = listOf(
            Game("g1","Elden Ring",49990,34990,"Acción RPG mundo abierto.","elden_ring"),
            Game("g2","Baldur's Gate 3",59990,49990,"CRPG por Larian Studios.","bg3"),
            Game("g3","Hades II",39990,null,"Rogue-like intenso.","hades2")
        )
        repo.seedIfEmpty(seed)
    }
}