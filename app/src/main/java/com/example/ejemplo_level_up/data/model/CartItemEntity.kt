package com.example.ejemplo_level_up.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val gameId: String,   // id del producto (Game.id)
    val quantity: Int
)