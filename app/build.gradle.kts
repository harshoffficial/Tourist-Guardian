plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.harsh.touristguardian"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.harsh.touristguardian"
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("androidx.core:core-splashscreen:1.0.0")
    implementation("com.airbnb.android:lottie:6.4.0")
    
    // Location Services (still using Google Play Services for location)
    implementation(libs.play.services.location)
    
    // REMOVED OsmDroid - using Leaflet.js in WebView instead
    // implementation("org.osmdroid:osmdroid-android:6.1.18")

    // OkHttp for API calls
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
}