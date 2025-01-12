plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id ("kotlin-parcelize")
    id("com.google.devtools.ksp") version "2.0.21-1.0.28"
}

android {
    namespace = "com.example.os_app_gertum1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.os_app_gertum1"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    sourceSets {
        getByName("main") {
            kotlin.srcDir("build/generated/ksp/main/kotlin")
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.coordinatorlayout)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    // Adding AppCompat dependency
    implementation(libs.androidx.appcompat)
    implementation( libs.androidx.room.runtime) // Room library
    implementation (libs.androidx.recyclerview) // RecyclerView library

    implementation("androidx.room:room-ktx:2.5.1")
    ksp("androidx.room:room-compiler:2.5.1") // Use KSP instead of kapt

//    // Http4k dependencies
//    implementation("org.http4k:http4k-core:4.18.0.0") // Core library
//    implementation("org.http4k:http4k-client-apache:4.18.0.0") // Client to make HTTP requests
//    implementation("org.http4k:http4k-format-jackson:4.18.0.0") // JSON serialization support
//
//    // Gson or Jackson for JSON serialization ///mmmm idk
//    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")

    // Map
    implementation ("org.osmdroid:osmdroid-android:6.1.10")  // Latest version

    // Žiniatinklio paslauga
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")
    implementation ("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation ("com.google.code.gson:gson:2.10.1")

    implementation ("com.squareup.okhttp3:okhttp:4.10.0")

    implementation ("androidx.navigation:navigation-fragment-ktx:2.8.5") // Adjust version as needed
    implementation ("androidx.navigation:navigation-ui-ktx:2.8.5")
}