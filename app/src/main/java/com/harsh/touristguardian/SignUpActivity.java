package com.harsh.touristguardian;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText usernameInput, emailInput, passwordInput;
    private MaterialButton signUpButton;
    private TextView signIn;
    
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        
        // Initialize Firebase Database
        // Try to get default instance first
        try {
            databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        } catch (Exception e) {
            // If default instance fails (configuration not found error),
            // try with explicit database URL
            try {
                // Use explicit database URL - replace with your actual database URL if different
                // Format: https://{project-id}-default-rtdb.{region}.firebasedatabase.app/
                // Or for older projects: https://{project-id}-default-rtdb.firebaseio.com/
                String databaseUrl = "https://tourist-guardian-a0d19-default-rtdb.firebaseio.com/";
                FirebaseDatabase database = FirebaseDatabase.getInstance(databaseUrl);
                databaseReference = database.getReference("Users");
            } catch (Exception ex) {
                // Show user-friendly error message
                Toast.makeText(this, 
                    "Firebase Database is not configured. Please enable Realtime Database in Firebase Console.", 
                    Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        }

        // Initialize views
        usernameInput = findViewById(R.id.usernameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        signUpButton = findViewById(R.id.signUpButton);
        signIn = findViewById(R.id.sign_in);

        // Initialize progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating account...");
        progressDialog.setCancelable(false);

        // Set click listeners
        signUpButton.setOnClickListener(v -> registerUser());

        signIn.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void registerUser() {
        String username = usernameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(username)) {
            usernameInput.setError("Username is required");
            usernameInput.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            emailInput.setError("Email is required");
            emailInput.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Please enter a valid email");
            emailInput.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Password is required");
            passwordInput.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordInput.setError("Password must be at least 6 characters");
            passwordInput.requestFocus();
            return;
        }

        // Check if database is initialized
        if (databaseReference == null) {
            Toast.makeText(this, 
                "Database not initialized. Please enable Realtime Database in Firebase Console.", 
                Toast.LENGTH_LONG).show();
            return;
        }

        // Show progress dialog
        progressDialog.show();

        // Create user with Firebase Auth
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // User created successfully
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            // Get user ID
                            String userId = firebaseUser.getUid();
                            
                            // Create user data map
                            HashMap<String, Object> userData = new HashMap<>();
                            userData.put("userId", userId);
                            userData.put("username", username);
                            userData.put("email", email);
                            // Note: Password is handled by Firebase Auth and should not be stored in Realtime Database
                            
                            // Update progress dialog message
                            progressDialog.setMessage("Saving user data...");
                            
                            // Save user data to Realtime Database
                            databaseReference.child(userId).setValue(userData)
                                    .addOnCompleteListener(task1 -> {
                                        progressDialog.dismiss();
                                        
                                        if (task1.isSuccessful()) {
                                            // Data saved successfully
                                            Toast.makeText(SignUpActivity.this, 
                                                    "Account created successfully!", 
                                                    Toast.LENGTH_SHORT).show();
                                            
                                            // Navigate to MainActivity
                                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            // Error saving data - provide more helpful error message
                                            Exception exception = task1.getException();
                                            String errorMessage = "Error saving user data.";
                                            if (exception != null) {
                                                String exceptionMessage = exception.getMessage();
                                                if (exceptionMessage != null) {
                                                    if (exceptionMessage.contains("configuration") || exceptionMessage.contains("Configuration")) {
                                                        errorMessage = "Database configuration error. Please enable Realtime Database in Firebase Console.";
                                                    } else if (exceptionMessage.contains("permission") || exceptionMessage.contains("Permission") || 
                                                               exceptionMessage.contains("permission_denied") || exceptionMessage.contains("PERMISSION_DENIED")) {
                                                        errorMessage = "Permission denied. Please update Firebase Database rules to allow authenticated users to write data.";
                                                    } else {
                                                        errorMessage = "Error: " + exceptionMessage;
                                                    }
                                                }
                                            }
                                            Toast.makeText(SignUpActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                                            
                                            // Log the full error for debugging
                                            if (exception != null) {
                                                exception.printStackTrace();
                                            }
                                        }
                                    });
                        }
                    } else {
                        progressDialog.dismiss();
                        // Sign up failed
                        Exception exception = task.getException();
                        String errorMessage = "Registration failed. Please try again.";
                        if (exception != null) {
                            String exceptionMessage = exception.getMessage();
                            if (exceptionMessage != null && exceptionMessage.contains("configuration")) {
                                errorMessage = "Firebase configuration error. Please check your Firebase setup.";
                            } else if (exceptionMessage != null) {
                                errorMessage = exceptionMessage;
                            }
                        }
                        Toast.makeText(SignUpActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}