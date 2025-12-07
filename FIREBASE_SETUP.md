# Firebase Database Setup Instructions

## Fix "Permission Denied" Error

The "Permission Denied" error occurs when Firebase Realtime Database security rules don't allow your app to write data. Follow these steps to fix it:

### Step 1: Open Firebase Console
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Select your project: `tourist-guardian-a0d19`
3. Click on **Realtime Database** in the left menu

### Step 2: Update Security Rules
1. Click on the **Rules** tab at the top
2. Replace the existing rules with one of the following options:

#### Option A: Secure Rules (Recommended for Production)
```json
{
  "rules": {
    "Users": {
      "$userId": {
        ".read": "auth != null && auth.uid == $userId",
        ".write": "auth != null && auth.uid == $userId"
      }
    }
  }
}
```
This allows users to read/write only their own data.

#### Option B: Development Rules (For Testing Only)
```json
{
  "rules": {
    "Users": {
      ".read": "auth != null",
      ".write": "auth != null"
    }
  }
}
```
This allows any authenticated user to read/write to the Users node.

⚠️ **Warning**: Option B is less secure and should only be used for development/testing.

#### Option C: Open Rules (⚠️ EXTREMELY INSECURE - Development Only!)
```json
{
  "rules": {
    ".read": true,
    ".write": true
  }
}
```
This allows **ANYONE** (even without authentication) to read and write **ALL** data in your database.

🚨 **CRITICAL WARNING**: 
- **NEVER use this in production!**
- Anyone can access, modify, or delete all your data
- Your database will be vulnerable to attacks
- Only use this for local development/testing
- **Change it immediately after testing!**

### Step 3: Publish Rules
1. After updating the rules, click **Publish** button
2. Wait a few seconds for the rules to propagate

### Step 4: Test the App
1. Run your app again
2. Try signing up - it should work now!

## Additional Notes

- Make sure **Realtime Database** is enabled in your Firebase project
- Make sure **Authentication** is enabled (Email/Password method)
- The rules file `database.rules.json` in the project root is for reference only - you need to update rules in Firebase Console

## Troubleshooting

If you still get permission errors:
1. Verify that Authentication is enabled in Firebase Console
2. Verify that the user is authenticated before trying to write (check Firebase Auth)
3. Check the Firebase Console logs for more details
4. Make sure you're using the correct database URL

