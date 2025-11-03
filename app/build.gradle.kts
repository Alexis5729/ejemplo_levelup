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
//    Se usa junto con "NavHost" y "AnimatedNavHost" en archivos como Navigation.kt o MainActivity.kt.
//    Ejemplo: al pasar del login a la pantalla de perfil, las pantallas se desplazan con efecto de transición.

// --- Carga de imágenes en Jetpack Compose ---
    implementation("io.coil-kt:coil-compose:2.6.0")
// ➜ Librería COIL (Coil = Coroutine Image Loader) que permite cargar imágenes desde URLs o almacenamiento interno.
//    En el proyecto, se usa en la pantalla de Perfil para mostrar o actualizar la foto del usuario.
//    Ejemplo de uso:
//    Image(painter = rememberImagePainter(user.profileImage), contentDescription = "Foto de perfil")

// --- Íconos extendidos de Material Design ---
    implementation("androidx.compose.material:material-icons-extended")
// ➜ Paquete completo de íconos de Material Design para Compose (por ejemplo, Icons.Filled.Home, Icons.Outlined.Person).
//    Se usa en botones, barras de navegación y menús de la app, principalmente en componentes de la UI como BottomNavigation.

// --- Navegación entre pantallas con Compose ---
    implementation("androidx.navigation:navigation-compose:2.8.3")
// ➜ Permite estructurar la navegación dentro de Compose sin usar fragments tradicionales.
//    En tu app se usa para conectar pantallas: Login → Home → Perfil → Configuración.
//    Ejemplo de uso:
//    NavHost(navController, startDestination = "login") { composable("perfil") { PerfilScreen() } }

// --- Manejo de estado con ViewModel integrado a Compose ---
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
// ➜ Permite crear y vincular ViewModels directamente con pantallas Compose.
//    Se usa para manejar la lógica de negocio y datos persistentes sin recargar la interfaz.
//    Ejemplo: El ViewModel del Login o del Perfil guarda los datos del usuario y estados de sesión.

// --- Base de datos local con Room ---
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
// ➜ Room es el ORM (Object Relational Mapping) de Android para usar SQLite fácilmente.
//    Se usa para guardar datos locales: usuarios, productos, historial de navegación, etc.
//    - runtime: la librería principal.
//    - compiler: genera automáticamente el código SQL a partir de tus @Dao y @Entity.
//    - ktx: añade funciones Kotlin-friendly y soporte para corrutinas.
//    Ejemplo en código:
//    @Entity data class Usuario(...)
//    @Dao interface UsuarioDao { @Query("SELECT * FROM usuario") fun getAll(): List<Usuario> }
//    val db = Room.databaseBuilder(context, AppDatabase::class.java, "levelup.db").build()

// --- DataStore: almacenamiento ligero de preferencias ---
    implementation("androidx.datastore:datastore-preferences:1.1.1")
// ➜ Reemplazo moderno de SharedPreferences para guardar configuraciones simples (modo oscuro, sesión, etc.).
//    Se usa para mantener el estado del usuario o sus preferencias de inicio de sesión persistentes.
//    Ejemplo en código:
//    val dataStore = context.createDataStore(name = "user_prefs")
//    dataStore.edit { it[stringPreferencesKey("username")] = "Marco" }

// --- Cámara: Captura y vista previa (CameraX) ---
    val camerax = "1.4.0"
    implementation("androidx.camera:camera-core:$camerax")
    implementation("androidx.camera:camera-camera2:$camerax")
    implementation("androidx.camera:camera-lifecycle:$camerax")
    implementation("androidx.camera:camera-view:$camerax")
// ➜ Conjunto de librerías CameraX para usar la cámara fácilmente sin código nativo complicado.
//    - core / camera2: acceso al hardware de cámara.
//    - lifecycle: gestiona automáticamente encendido/apagado según ciclo de vida.
//    - view: muestra la vista previa en pantalla Compose o XML.
//    En la app, se usa para funciones como escanear QR o tomar foto de perfil.

// --- ML Kit: escaneo de códigos QR y de barras ---
    implementation("com.google.mlkit:barcode-scanning:17.3.0")
// ➜ Usa la API de Machine Learning de Google para detectar códigos QR a través de la cámara.
//    Se integra con CameraX para procesar frames en tiempo real y obtener resultados.
//    Ejemplo en código:
//    val scanner = BarcodeScanning.getClient()
//    val image = InputImage.fromMediaImage(mediaImage, rotationDegrees)
//    scanner.process(image).addOnSuccessListener { barcodes -> ... }

    // --- Tests ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
