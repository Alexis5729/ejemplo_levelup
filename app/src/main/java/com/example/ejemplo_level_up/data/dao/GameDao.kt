package com.example.ejemplo_level_up.data.dao

import androidx.room.*
import com.example.ejemplo_level_up.data.model.Game
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Query("SELECT * FROM games ORDER BY title")
    fun getAll(): Flow<List<Game>>

    @Query("SELECT * FROM games WHERE id = :id LIMIT 1")
    fun getById(id: String): Flow<Game?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<Game>)
}
