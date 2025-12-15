package com.example.ejemplo_level_up.data.repository

import com.example.ejemplo_level_up.data.dao.CartDao
import com.example.ejemplo_level_up.data.model.CartItemEntity
import kotlinx.coroutines.flow.Flow

class CartRepository(private val dao: CartDao) {

    fun items(): Flow<List<CartItemEntity>> = dao.getAll()

    suspend fun addOne(gameId: String) {
        val current = dao.getById(gameId)
        val newQty = (current?.quantity ?: 0) + 1
        dao.upsert(CartItemEntity(gameId, newQty))
    }

    suspend fun setQuantity(gameId: String, quantity: Int) {
        if (quantity <= 0) dao.deleteById(gameId)
        else dao.upsert(CartItemEntity(gameId, quantity))
    }

    suspend fun remove(gameId: String) = dao.deleteById(gameId)

    suspend fun clear() = dao.clear()
}
