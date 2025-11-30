plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("kapt") // Para Room
}


android {
    namespace = "com.example.ejemplo_level_up"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.ejemplo_level_up"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }

    buildFeatures { compose = true }
}

dependencies {
    // --- Compose básico (mismo esquema que login002v) ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // --- Animaciones entre pantallas con Compose Navigation ---
    implementation("com.google.accompanist:accompanist-navigation-animation:0.36.0")
    // ➜ Librería de soporte de Google "Accompanist" que añade animaciones suaves al navegar entre pantallas.

    // --- Carga de imágenes en Jetpack Compose ---
    implementation("io.coil-kt:coil-compose:2.6.0")
    // ➜ Librería COIL (Coroutine Image Loader) para cargar imágenes.

    // --- Íconos extendidos de Material Design ---
    implementation("androidx.compose.material:material-icons-extended")
    // ➜ Paquete completo de íconos de Material Design para Compose.

    // --- Navegación entre pantallas con Compose ---
    implementation("androidx.navigation:navigation-compose:2.8.3")
    // ➜ Permite estructurar la navegación dentro de Compose.

    // --- Manejo de estado con ViewModel integrado a Compose ---
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
    // ➜ ViewModel + Compose.

    // --- Base de datos local con Room ---
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    // ➜ Room ORM para SQLite.

    // --- DataStore: almacenamiento ligero de preferencias ---
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    // ➜ Reemplazo moderno de SharedPreferences.

    // --- Cámara: Captura y vista previa (CameraX) ---
    val camerax = "1.4.0"
    implementation("androidx.camera:camera-core:$camerax")
    implementation("androidx.camera:camera-camera2:$camerax")
    implementation("androidx.camera:camera-lifecycle:$camerax")
    implementation("androidx.camera:camera-view:$camerax")
    // ➜ Librerías CameraX.

    // --- ML Kit: escaneo de códigos QR y de barras ---
    implementation("com.google.mlkit:barcode-scanning:17.3.0")
    // ➜ API de ML Kit para leer códigos de barra / QR.

    // --- Google Maps SDK para Android (Mapa de sucursales) ---
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    // ➜ Necesario para usar MapView, GoogleMap, markers, etc.

    // --- Tests ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
