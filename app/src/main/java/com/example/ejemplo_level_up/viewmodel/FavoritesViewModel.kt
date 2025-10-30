package com.example.ejemplo_level_up.viewmodel

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.map

private val Context.ds by preferencesDataStore("prefs")
private val FAVS = stringSetPreferencesKey("favs")

class FavoritesViewModel(app: Application) : AndroidViewModel(app) {
    private val ctx = app.applicationContext
    val favIds = ctx.ds.data.map { it[FAVS] ?: emptySet() }
    suspend fun toggle(id: String) {
        ctx.ds.edit { p ->
            val now = p[FAVS] ?: emptySet()
            p[FAVS] = if (id in now) now - id else now + id
        }
    }
}