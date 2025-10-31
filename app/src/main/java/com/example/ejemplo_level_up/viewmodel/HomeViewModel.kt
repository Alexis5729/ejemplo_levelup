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
            Game("g101","HyperX Cloud II Wireless", 59990, 49990, "Audífonos inalámbricos con sonido 7.1.", "hyperx_cloud2"),
            Game("g102","Catan – El Juego",           34990, 29990, "Juego de mesa clásico de estrategia.", "catan"),
            Game("g103","ASUS ROG Strix 16 RTX 4070",1999990,1799990,"Notebook gamer de alto rendimiento.", "rog_strix_4070"),
            Game("g104","Polera Level-Up Gamer",       12990,  9990, "Polera temática Level-Up Gamer.", "polera_levelup"),
            Game("g105","PlayStation 5 (Slim)",       649990,599990,"Consola de última generación.", "ps5_box"),
            Game("g106","Control Xbox – Carbon Black", 49990, 44990, "Control inalámbrico Xbox.", "xbox_controller"),
            Game("g107","Logitech G502 HERO",          39990, 34990, "Mouse gamer con sensor HERO 25K.", "logitech_g502")
        )
        repo.seedIfEmpty(seed)
    }
}