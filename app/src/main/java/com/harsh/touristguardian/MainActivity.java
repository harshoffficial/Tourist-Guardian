package com.harsh.touristguardian;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private FirebaseAuth firebaseAuth;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageButton menuButton;
    private ProgressBar locationProgressBar;
    
    // WebView for Leaflet.js Map
    private WebView mapView;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize views
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        menuButton = findViewById(R.id.menuButton);
        locationProgressBar = findViewById(R.id.locationProgressBar);

        // Setup navigation drawer
        setupNavigationDrawer();

        // Check if user is logged in
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            redirectToLogin();
            return;
        }

        // Update navigation header with user info
        updateNavigationHeader(currentUser);

        // Set menu button listener
        menuButton.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.END));

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize WebView Map
        initializeMap();
    }

    /**
     * Setup Navigation Drawer
     */
    private void setupNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener(this);
        
        // Set navigation item icons to be colored
        navigationView.setItemIconTintList(null);
    }

    /**
     * Update navigation header with user info
     */
    private void updateNavigationHeader(FirebaseUser user) {
        View headerView = navigationView.getHeaderView(0);
        TextView navHeaderSubtitle = headerView.findViewById(R.id.navHeaderSubtitle);
        
        if (user != null && user.getEmail() != null) {
            navHeaderSubtitle.setText(user.getEmail());
        }
    }

    /**
     * Handle navigation menu item selection
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        
        if (itemId == R.id.nav_sos) {
            handleSOS();
        } else if (itemId == R.id.nav_share_location) {
            shareLocation();
        } else if (itemId == R.id.nav_danger_zones) {
            // Already on danger zones page
            Toast.makeText(this, "You are viewing danger zones", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.nav_safety_tips) {
            showSafetyTips();
        } else if (itemId == R.id.nav_settings) {
            showSettings();
        } else if (itemId == R.id.nav_about) {
            showAbout();
        } else if (itemId == R.id.nav_logout) {
            logoutUser();
        } else if (itemId == R.id.nav_ai_planner) {
            Intent intent = new Intent(MainActivity.this, AITravelPlannerActivity.class);
            startActivity(intent);
        }
        
        // Close drawer after selection
        drawerLayout.closeDrawer(GravityCompat.END);
        return true;
    }

    /**
     * Handle SOS Emergency
     */
    private void handleSOS() {
        if (currentLocation == null) {
            Toast.makeText(this, "Getting your location...", Toast.LENGTH_SHORT).show();
            getCurrentLocationForSOS();
            return;
        }
        
        // Create SOS message with location
        String sosMessage = "🚨 SOS EMERGENCY 🚨\n\n" +
                "I need immediate help!\n\n" +
                "My Location:\n" +
                "Latitude: " + currentLocation.getLatitude() + "\n" +
                "Longitude: " + currentLocation.getLongitude() + "\n\n" +
                "Google Maps: https://maps.google.com/?q=" + 
                currentLocation.getLatitude() + "," + currentLocation.getLongitude();
        
        // Call emergency number
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:112")); // Emergency number (112 in India)
        startActivity(callIntent);
        
        // Share location via SMS/WhatsApp
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, sosMessage);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "SOS Emergency - Help Needed");
        startActivity(Intent.createChooser(shareIntent, "Share SOS Alert"));
        
        Toast.makeText(this, "SOS Alert Activated!", Toast.LENGTH_LONG).show();
    }

    /**
     * Get current location for SOS
     */
    private void getCurrentLocationForSOS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Location permission required for SOS", Toast.LENGTH_LONG).show();
            return;
        }

        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setMaxUpdates(1)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null && locationResult.getLastLocation() != null) {
                    currentLocation = locationResult.getLastLocation();
                    handleSOS();
                    stopLocationUpdates();
                }
            }
        };

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, getMainLooper());
    }

    /**
     * Share current location
     */
    private void shareLocation() {
        if (currentLocation == null) {
            Toast.makeText(this, "Getting your location...", Toast.LENGTH_SHORT).show();
            getCurrentLocationForShare();
            return;
        }
        
        String locationMessage = "My Current Location:\n" +
                "Latitude: " + currentLocation.getLatitude() + "\n" +
                "Longitude: " + currentLocation.getLongitude() + "\n\n" +
                "Google Maps: https://maps.google.com/?q=" + 
                currentLocation.getLatitude() + "," + currentLocation.getLongitude();
        
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, locationMessage);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My Location");
        startActivity(Intent.createChooser(shareIntent, "Share Location"));
    }

    /**
     * Get current location for sharing
     */
    private void getCurrentLocationForShare() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setMaxUpdates(1)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null && locationResult.getLastLocation() != null) {
                    currentLocation = locationResult.getLastLocation();
                    shareLocation();
                    stopLocationUpdates();
                }
            }
        };

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, getMainLooper());
    }

    /**
     * Show Safety Tips
     */
    private void showSafetyTips() {
        Toast.makeText(this, "Safety Tips feature coming soon!", Toast.LENGTH_SHORT).show();
        // TODO: Implement safety tips activity
    }

    /**
     * Show Settings
     */
    private void showSettings() {
        Toast.makeText(this, "Settings feature coming soon!", Toast.LENGTH_SHORT).show();
        // TODO: Implement settings activity
    }

    /**
     * Show About
     */
    private void showAbout() {
        Toast.makeText(this, "Tourist Guardian - Your Safety Companion", Toast.LENGTH_LONG).show();
        // TODO: Implement about dialog
    }

    private void logoutUser() {
        firebaseAuth.signOut();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        redirectToLogin();
    }

    private void redirectToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            redirectToLogin();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Initialize WebView with Leaflet.js map
     */
    private void initializeMap() {
        mapView = findViewById(R.id.mapView);
        
        // Configure WebView
        WebSettings webSettings = mapView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        
        // Add JavaScript interface to communicate with Android
        mapView.addJavascriptInterface(new WebAppInterface(), "AndroidInterface");
        
        // Set WebViewClient to handle page loading
        mapView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Page loaded, now request location
                requestLocationPermissionAndGetLocation();
            }
        });
        
        // Load HTML file
        mapView.loadUrl("file:///android_asset/map.html");
    }

    /**
     * JavaScript interface to communicate with WebView
     */
    public class WebAppInterface {
        @JavascriptInterface
        public void onMapReady() {
            // Map is ready, request location
            runOnUiThread(() -> requestLocationPermissionAndGetLocation());
        }
    }

    /**
     * Request location permission and get current location
     */
    private void requestLocationPermissionAndGetLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission is required", Toast.LENGTH_LONG).show();
                locationProgressBar.setVisibility(android.view.View.GONE);
            }
        }
    }

    /**
     * Get current location and update map
     */
    private void getCurrentLocation() {
        locationProgressBar.setVisibility(android.view.View.VISIBLE);
        
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationProgressBar.setVisibility(android.view.View.GONE);
            return;
        }

        // Request high accuracy GPS location
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setMinUpdateIntervalMillis(1000)
                .setMaxUpdateDelayMillis(5000)
                .setMaxUpdates(1)
                .setWaitForAccurateLocation(true)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null && locationResult.getLastLocation() != null) {
                    Location location = locationResult.getLastLocation();
                    currentLocation = location; // Store for SOS/Share
                    updateMapLocation(location);
                    stopLocationUpdates();
                }
            }
        };

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, getMainLooper());
    }

    /**
     * Update map with location
     */
    private void updateMapLocation(Location location) {
        if (mapView == null || location == null) {
            locationProgressBar.setVisibility(android.view.View.GONE);
            return;
        }

        double lat = location.getLatitude();
        double lng = location.getLongitude();
        
        // Validate coordinates
        if (Math.abs(lat) > 90 || Math.abs(lng) > 180) {
            Toast.makeText(this, "Invalid location coordinates", Toast.LENGTH_SHORT).show();
            locationProgressBar.setVisibility(android.view.View.GONE);
            return;
        }

        // Call JavaScript function to update map
        String jsCode = "javascript:onLocationUpdate(" + lat + ", " + lng + ");";
        mapView.evaluateJavascript(jsCode, null);
        
        locationProgressBar.setVisibility(android.view.View.GONE);
        Toast.makeText(this, "Location found!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Stop location updates
     */
    private void stopLocationUpdates() {
        if (locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
            locationCallback = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
        if (mapView != null) {
            mapView.destroy();
        }
    }
}