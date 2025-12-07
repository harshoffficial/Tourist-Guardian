package com.harsh.touristguardian;

import android.app.Application;

import com.google.firebase.FirebaseApp;

/**
 * Custom Application class for TouristGuardian app.
 * 
 * This class runs once when the app starts, BEFORE any Activity is created.
 * It's the perfect place for app-wide initialization code.
 * 
 * Purpose:
 * 1. Initialize Firebase early to prevent "configuration not found" errors
 * 2. Centralized place for app-wide setup (analytics, crash reporting, etc.)
 * 3. Can store global variables that need to persist across activities
 * 
 * How it works:
 * - Registered in AndroidManifest.xml with android:name=".TouristGuardianApplication"
 * - Android automatically creates an instance when the app launches
 * - onCreate() is called before any Activity.onCreate()
 */
public class TouristGuardianApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        
        // Initialize Firebase - this ensures Firebase is initialized early
        // The google-services.json plugin usually handles this, but explicit initialization
        // ensures it's done before any Firebase services are used
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }
        
        // Note: FirebaseDatabase initialization is handled in individual Activities
        // (SignUpActivity, MainActivity) with proper error handling and fallback to explicit URL
        
        // Future: You can add other app-wide initialization here, such as:
        // - Analytics initialization (Firebase Analytics, Google Analytics)
        // - Crash reporting (Firebase Crashlytics)
        // - Global configuration settings
        // - Third-party SDK initialization
    }
}

