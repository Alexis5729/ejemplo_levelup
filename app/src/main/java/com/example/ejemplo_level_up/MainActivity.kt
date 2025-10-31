package com.example.ejemplo_level_up

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.ejemplo_level_up.navigation.AppNav
import com.example.ejemplo_level_up.ui.theme.EjemplolevelupTheme // ajusta al nombre real de tu Theme.kt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EjemplolevelupTheme(darkTheme = true, dynamicColor = false) {
                val nav = rememberNavController()
                AppNav(nav)
            }
        }
    }
}