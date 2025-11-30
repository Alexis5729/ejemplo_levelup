package com.example.ejemplo_level_up.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CartViewModel : ViewModel() {

    // id del producto -> cantidad
    private val _items = MutableStateFlow<Map<String, Int>>(emptyMap())
    val items: StateFlow<Map<String, Int>> = _items

    // CREATE / UPDATE (+1)
    fun add(id: String) {
        val current = _items.value.toMutableMap()
        val oldQty = current[id] ?: 0
        current[id] = oldQty + 1
        _items.value = current
    }

    // UPDATE: setear cantidad exacta
    fun setQuantity(id: String, quantity: Int) {
        val current = _items.value.toMutableMap()
        if (quantity <= 0) {
            current.remove(id) // si llega a 0, lo sacamos
        } else {
            current[id] = quantity
        }
        _items.value = current
    }

    // DELETE
    fun remove(id: String) {
        val current = _items.value.toMutableMap()
        current.remove(id)
        _items.value = current
    }

    // Opcional: vaciar carrito
    fun clear() {
        _items.value = emptyMap()
    }
}