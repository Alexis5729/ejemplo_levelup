package com.example.ejemplo_level_up.data

import android.content.Context
import android.content.SharedPreferences

class UserManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    // 🔹 Registrar un nuevo usuario
    fun registerUser(email: String, password: String) {
        prefs.edit()
            .putString("user_$email", password)
            .apply()
        setLastEmail(email) // Guarda el último correo registrado
    }

    // 🔹 Verificar si el usuario ya está registrado
    fun isUserRegistered(email: String): Boolean {
        return prefs.contains("user_$email")
    }

    // 🔹 Validar correo + contraseña
    fun validateUser(email: String, password: String): Boolean {
        val storedPass = prefs.getString("user_$email", null)
        return storedPass == password
    }

    // 🔹 Guardar usuario logueado actualmente
    fun setLoggedInUser(email: String) {
        prefs.edit()
            .putString("logged_in_user", email)
            .apply()
    }

    // 🔹 Obtener usuario logueado actualmente
    fun getLoggedInUser(): String? {
        return prefs.getString("logged_in_user", null)
    }

    // 🔹 Cerrar sesión (borra solo la sesión activa)
    fun clearLoggedInUser() {
        prefs.edit().remove("logged_in_user").apply()
    }

    // 🔹 Guardar el último correo usado (para autocompletar)
    fun setLastEmail(email: String) {
        prefs.edit().putString("last_email", email).apply()
    }

    // 🔹 Obtener el último correo usado
    fun getLastEmail(): String? {
        return prefs.getString("last_email", null)
    }

    // 🔹 Eliminar todos los usuarios (solo para pruebas o reset total)
    fun clearAllUsers() {
        prefs.edit().clear().apply()
    }
}
