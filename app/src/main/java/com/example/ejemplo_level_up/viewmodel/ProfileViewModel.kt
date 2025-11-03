package com.example.ejemplo_level_up.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {
    var imageBitmap: Bitmap? = null
    var imageUri: Uri? = null
}
