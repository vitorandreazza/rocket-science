plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlinAndroid)
    id("kotlin-parcelize")
    id("kotlinx-serialization")
    kotlin("kapt")
}

android {
    namespace = "com.example.rocketscience"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.rocketscience"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            buildConfigField("String", "SPACEX_BASE_URL", "\"https://api.spacexdata.com/v3/\"")

            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            buildConfigField("String", "SPACEX_BASE_URL", "\"https://api.spacexdata.com/v3/\"")
        }
    }
    kotlin {
        jvmToolchain(17)
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
}

dependencies {
    implementation(libs.activity.compose)
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.core.ktx)
    implementation(libs.hilt.android)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.lifecycle.compose)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.material3)
    implementation(libs.ui)
    implementation(libs.ui.tooling.preview)

    testImplementation(libs.junit)
    testImplementation(libs.mockito)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(kotlin("test"))

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ui.test.junit4)

    kapt(libs.hilt.compiler)
}
