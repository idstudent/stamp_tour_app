plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.ljystamp.feature_my"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

dependencies {
    implementation(project(":core_model"))
    implementation(project(":core_network"))
    implementation(project(":core_ui"))
    implementation(project(":core_utils"))
    implementation(project(":common"))
    implementation(project(":feature_auth"))
    implementation(project(":feature_my_tour"))
    implementation(project(":feature_my_tour_detail"))
    implementation(project(":core_navigation"))


    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")

    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    implementation("com.google.android.flexbox:flexbox:3.0.0")

    implementation("androidx.compose.material3:material3:1.3.1")

    implementation("androidx.compose.foundation:foundation:1.5.4")
    implementation("io.coil-kt:coil-compose:2.5.0")

    implementation("com.google.code.gson:gson:2.10.1")
}