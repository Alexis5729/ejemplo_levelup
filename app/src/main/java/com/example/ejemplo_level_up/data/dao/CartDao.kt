package com.example.ejemplo_level_up.data.dao

import androidx.room.*
import com.example.ejemplo_level_up.data.model.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    // READ
    @Query("SELECT * FROM cart_items")
    fun getAll(): Flow<List<CartItemEntity>>

    // READ (opcional)
    @Query("SELECT * FROM cart_items WHERE gameId = :id LIMIT 1")
    suspend fun getById(id: String): CartItemEntity?

    // CREATE/UPDATE (upsert)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: CartItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<CartItemEntity>)

    // DELETE
    @Query("DELETE FROM cart_items WHERE gameId = :id")
    suspend fun deleteById(id: String)

    // CLEAR
    @Query("DELETE FROM cart_items")
    suspend fun clear()
}
