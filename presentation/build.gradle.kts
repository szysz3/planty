plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.services)
    id("kotlin-kapt")
}

android {
    namespace = "szysz3.planty"
    compileSdk = 35

    defaultConfig {
        applicationId = "szysz3.planty"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    android.buildFeatures.buildConfig = true

    val apiKey: String = System.getenv("API_KEY")
        ?: throw IllegalStateException("API_KEY environment variable is not set")

    buildTypes {
        debug {
            buildConfigField("String", "API_KEY", "\"$apiKey\"")
        }
        release {
            buildConfigField("String", "API_KEY", "\"$apiKey\"")
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

    implementation(project(":domain"))
    // This is needed because of Hilt. Domain depends on data and we inject use cases
    // so domain dependency has to be added as well. Hilt cannot locate repositories otherwise.
    implementation(project(":data"))

    // room dependencies
    implementation(libs.room.runtime)
    implementation(libs.androidx.constraintlayout)
    kapt(libs.room.compiler)
    implementation(libs.room.ktx)

    // coroutines dependencies
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    //coil
    implementation(libs.coil)

    // hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.compose)
    kapt(libs.hilt.compiler)

    //paging
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)

    // zoomable
    implementation(libs.zoomable)

    implementation(libs.timber)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material)
    implementation(libs.androidx.navigation.compose)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.arch.core.testing)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}