import org.gradle.kotlin.dsl.implementation

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.example.animepoc"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.animepoc"
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
        viewBinding = true
    }

    dataBinding {
        true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor.v490)
    api(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.glide)
    annotationProcessor(libs.compiler)
    // --- Room (Database) ---
    implementation(libs.androidx.room.runtime)
    kapt(libs.room.compiler)

    // Kotlin Extensions and Coroutines support for Room
    implementation(libs.androidx.room.ktx)


//        // --- Lifecycle (ViewModel + LiveData + Coroutines support) ---
//        implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6"
//        implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.8.6"
//        implementation "androidx.lifecycle:lifecycle-runtime-ktx:2.8.6"
//
//        // --- Coroutines ---
//        implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0"
//        implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0"
//
//        // --- Retrofit (Networking) ---
//        implementation "com.squareup.retrofit2:retrofit:2.11.0"
//        implementation "com.squareup.retrofit2:converter-gson:2.11.0"
//
//        // --- OkHttp (Networking + Logging) ---
//        implementation "com.squareup.okhttp3:okhttp:5.0.0-alpha.14"
//        implementation "com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.14"
//
//
//        // --- Network Connectivity ---
//        implementation "androidx.core:core-ktx:1.13.1"
//
//        // Optional: if you plan to use Paging 3 with Room
//        implementation "androidx.paging:paging-runtime:3.3.2"
//    }


}