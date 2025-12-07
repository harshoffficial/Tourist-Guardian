# Template Reuse Guide: Firebase Authentication

This guide explains what you need to change when reusing this authentication template for a new app.

## ✅ What You CAN Keep (Template Code)

Most of the code structure is reusable! The Firebase Authentication logic, validation, error handling, and flow remain the same.

## 🔧 What You MUST Change

### 1. **Package Name** (CRITICAL - Change in ALL Java files)

**Files to update:**
- `LoginActivity.java`
- `SignUpActivity.java`
- `MainActivity.java`
- `TouristGuardianApplication.java`
- All other Java files

**Change:**
```java
// OLD
package com.harsh.touristguardian;

// NEW (replace with your package name)
package com.yourcompany.yourappname;
```

### 2. **Firebase Database URL** (SignUpActivity.java & MainActivity.java)

**Location:** `SignUpActivity.java` line 60, `MainActivity.java` line 49

**Change:**
```java
// OLD
String databaseUrl = "https://tourist-guardian-a0d19-default-rtdb.firebaseio.com/";

// NEW (replace with your Firebase project database URL)
String databaseUrl = "https://YOUR-PROJECT-ID-default-rtdb.firebaseio.com/";
// OR for newer projects:
String databaseUrl = "https://YOUR-PROJECT-ID-default-rtdb.REGION.firebasedatabase.app/";
```

**How to find your database URL:**
1. Go to Firebase Console
2. Select your project
3. Go to Realtime Database
4. Copy the database URL from the top

### 3. **Database Reference Path** (Optional - if you want different structure)

**Location:** `SignUpActivity.java` line 52, `MainActivity.java` line 46

**Change:**
```java
// OLD
databaseReference = FirebaseDatabase.getInstance().getReference("Users");

// NEW (if you want a different node name)
databaseReference = FirebaseDatabase.getInstance().getReference("YourNodeName");
```

### 4. **Activity Class Names** (If you rename activities)

**Files to update:**
- `LoginActivity.java` - Intent references
- `SignUpActivity.java` - Intent references
- `MainActivity.java` - Intent references

**Change:**
```java
// OLD
Intent intent = new Intent(LoginActivity.this, MainActivity.class);

// NEW (if you renamed MainActivity to HomeActivity)
Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
```

### 5. **Layout File IDs** (If you change layout files)

**Location:** All Activity files - `findViewById()` calls

**Make sure layout IDs match:**
- `R.id.emailInput`
- `R.id.passwordInput`
- `R.id.usernameInput` (SignUp only)
- `R.id.loginButton` / `R.id.signUpButton`
- `R.id.signUp` / `R.id.sign_in`
- `R.id.logoutButton` (MainActivity)
- etc.

### 6. **User Data Fields** (SignUpActivity.java - Optional)

**Location:** `SignUpActivity.java` lines 152-155

**Current fields saved:**
```java
userData.put("userId", userId);
userData.put("username", username);
userData.put("email", email);
```

**You can add more fields:**
```java
userData.put("userId", userId);
userData.put("username", username);
userData.put("email", email);
userData.put("phoneNumber", phoneNumber);  // If you add phone input
userData.put("fullName", fullName);        // If you add name input
userData.put("dateOfBirth", dateOfBirth);  // If you add DOB input
// Add any custom fields you need
```

### 7. **Progress Dialog Messages** (Optional - for customization)

**Location:** 
- `LoginActivity.java` line 47
- `SignUpActivity.java` line 81, 159

**Change:**
```java
// OLD
progressDialog.setMessage("Logging in...");

// NEW (customize messages)
progressDialog.setMessage("Signing you in...");
progressDialog.setMessage("Creating your account...");
progressDialog.setMessage("Saving your profile...");
```

### 8. **Toast Messages** (Optional - for customization)

**Location:** Multiple places in both files

**You can customize:**
- Success messages
- Error messages
- Validation messages

### 9. **Password Validation Rules** (SignUpActivity.java - Optional)

**Location:** `SignUpActivity.java` line 124

**Current rule:**
```java
if (password.length() < 6) {
    // Error: Password must be at least 6 characters
}
```

**You can make it stricter:**
```java
if (password.length() < 8) {
    // Error: Password must be at least 8 characters
}
// Add more validation: uppercase, lowercase, numbers, special characters
```

### 10. **Username Validation** (SignUpActivity.java - Optional)

**Location:** `SignUpActivity.java` line 100

**Current:** Just checks if empty

**You can add:**
```java
// Check username length
if (username.length() < 3) {
    usernameInput.setError("Username must be at least 3 characters");
    return;
}

// Check for special characters
if (!username.matches("^[a-zA-Z0-9_]+$")) {
    usernameInput.setError("Username can only contain letters, numbers, and underscores");
    return;
}
```

## 📋 Step-by-Step Checklist for New App

### Firebase Setup:
- [ ] Create new Firebase project
- [ ] Enable Authentication (Email/Password)
- [ ] Enable Realtime Database
- [ ] Download new `google-services.json`
- [ ] Replace `google-services.json` in `app/` folder
- [ ] Update database URL in code (SignUpActivity, MainActivity)
- [ ] Set up Firebase Database security rules

### Code Changes:
- [ ] Update package name in all Java files
- [ ] Update `build.gradle.kts` - applicationId
- [ ] Update `AndroidManifest.xml` - package name
- [ ] Update database URL in SignUpActivity.java
- [ ] Update database URL in MainActivity.java
- [ ] Update Activity class names (if renamed)
- [ ] Update layout file IDs (if changed)
- [ ] Add/remove user data fields (optional)
- [ ] Customize messages (optional)
- [ ] Update validation rules (optional)

### Application Class:
- [ ] Rename `TouristGuardianApplication.java` to `YourAppApplication.java`
- [ ] Update package name in Application class
- [ ] Update `AndroidManifest.xml` - android:name=".YourAppApplication"

## 🎯 Minimal Changes Required (Must Do)

For a basic working copy, you ONLY need to change:

1. **Package name** (all Java files)
2. **Database URL** (SignUpActivity.java line 60, MainActivity.java line 49)
3. **google-services.json** file (download from your Firebase project)
4. **Application class name** (if you rename it)

Everything else is optional customization!

## 📝 Example: Complete Change for New App

### New App Details:
- Package: `com.example.mynewapp`
- Firebase Project: `my-new-app-12345`
- Database URL: `https://my-new-app-12345-default-rtdb.firebaseio.com/`

### Changes:

**1. LoginActivity.java:**
```java
package com.example.mynewapp;  // Changed package
// Rest of the code stays the same!
```

**2. SignUpActivity.java:**
```java
package com.example.mynewapp;  // Changed package

// Line 60 - Change database URL
String databaseUrl = "https://my-new-app-12345-default-rtdb.firebaseio.com/";
// Rest stays the same!
```

**3. MainActivity.java:**
```java
package com.example.mynewapp;  // Changed package

// Line 49 - Change database URL
String databaseUrl = "https://my-new-app-12345-default-rtdb.firebaseio.com/";
// Rest stays the same!
```

**4. Replace google-services.json:**
- Delete old `app/google-services.json`
- Add new one from Firebase Console

That's it! The template code handles the rest. 🎉

## 🚀 Quick Start Template

```java
// LoginActivity.java - MINIMAL CHANGES NEEDED
package com.yourpackage.yourapp;  // ← CHANGE THIS

public class LoginActivity extends AppCompatActivity {
    // Everything else stays the same!
    // Firebase Auth code is universal
    // Validation logic is universal
    // Error handling is universal
}
```

```java
// SignUpActivity.java - MINIMAL CHANGES NEEDED
package com.yourpackage.yourapp;  // ← CHANGE THIS

// Line 60 - CHANGE DATABASE URL
String databaseUrl = "https://YOUR-PROJECT-ID-default-rtdb.firebaseio.com/";  // ← CHANGE THIS

// Everything else stays the same!
```

## 💡 Pro Tips

1. **Keep the error handling** - It's well-tested and handles common Firebase errors
2. **Keep the validation** - Email and password validation are standard
3. **Customize messages** - Make them match your app's tone
4. **Add fields as needed** - Easy to extend the user data structure
5. **Test thoroughly** - After changing package name and database URL, test login/signup flow

## 🔍 What's Universal (No Changes Needed)

- Firebase Authentication logic
- Email validation
- Password validation
- Error handling patterns
- Progress dialogs
- Navigation flow (Login → Main, SignUp → Main)
- Auto-login check (onStart method)
- Logout functionality

These are standard Firebase patterns that work for any app!

