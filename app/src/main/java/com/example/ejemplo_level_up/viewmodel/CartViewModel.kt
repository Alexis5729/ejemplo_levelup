package com.example.ejemplo_level_up.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ejemplo_level_up.data.database.GameDatabase
import com.example.ejemplo_level_up.data.repository.CartRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = CartRepository(GameDatabase.getInstance(app).cartDao())

    val items = repo.items()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun add(id: String) = viewModelScope.launch { repo.addOne(id) }
    fun setQuantity(id: String, quantity: Int) = viewModelScope.launch { repo.setQuantity(id, quantity) }
    fun remove(id: String) = viewModelScope.launch { repo.remove(id) }
    fun clear() = viewModelScope.launch { repo.clear() }
}
