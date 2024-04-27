plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")

    id ("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp")

}

android {
    namespace = "uk.ac.tees.mad.w9611189"
    compileSdk = 34

    defaultConfig {
        applicationId = "uk.ac.tees.mad.w9611189"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("androidx.core:core-splashscreen:1.0.1")        // splash screen

    implementation(libs.firebase.auth)                            // Firebase Authentication
    implementation("com.google.firebase:firebase-auth-ktx:22.3.1")
    implementation("com.google.android.gms:play-services-auth:21.0.0")

//    implementation("com.google.dagger:hilt-android:2.44")   // hilt
//    kapt("com.google.dagger:hilt-android-compiler:2.44")
//    kapt ("androidx.hilt:hilt-compiler:1.2.0")


    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation ("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha01")
    implementation ("com.google.dagger:hilt-android:2.28-alpha")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")   // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0") // ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")     // live data
    implementation("androidx.compose.runtime:runtime-livedata:1.6.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0") // ViewModel utilities for Compose

    implementation("androidx.navigation:navigation-compose:2.7.7") // Navigation

//    Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.47")
    ksp ("com.google.dagger:hilt-compiler:2.47")
    implementation ("androidx.hilt:hilt-navigation-compose:1.0.0")
}