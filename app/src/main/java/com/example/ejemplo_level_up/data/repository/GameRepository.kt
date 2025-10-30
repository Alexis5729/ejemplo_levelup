package com.example.ejemplo_level_up.data.repository

import com.example.ejemplo_level_up.data.dao.GameDao
import com.example.ejemplo_level_up.data.model.Game
import kotlinx.coroutines.flow.Flow

class GameRepository(private val dao: GameDao) {
    fun allGames(): Flow<List<Game>> = dao.getAll()
    fun gameById(id: String): Flow<Game?> = dao.getById(id)
    suspend fun seedIfEmpty(seed: List<Game>) = dao.upsertAll(seed)
}