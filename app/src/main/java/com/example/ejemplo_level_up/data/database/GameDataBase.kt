package com.example.ejemplo_level_up.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ejemplo_level_up.data.dao.GameDao
import com.example.ejemplo_level_up.data.model.Game

@Database(entities = [Game::class], version = 1, exportSchema = false)
abstract class GameDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao

    companion object {
        @Volatile private var INSTANCE: GameDatabase? = null
        fun getInstance(context: Context): GameDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    GameDatabase::class.java, "levelup.db"
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
    }
}