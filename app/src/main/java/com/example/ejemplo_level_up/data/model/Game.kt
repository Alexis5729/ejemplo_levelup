package com.example.ejemplo_level_up.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class Game(
    @PrimaryKey val id: String,
    val title: String,
    val price: Int,
    val offerPrice: Int?,
    val description: String,
    val imageResName: String // nombre del drawable (ej: "Catan")
)
