# Google Maps Setup Guide

## Step 1: Enable Maps SDK for Android

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Sign in with your Google account
3. Select your project: **tourist-guardian-a0d19**
4. Navigate to **APIs & Services** → **Library**
5. Search for "**Maps SDK for Android**"
6. Click on it and press **Enable**

## Step 2: Get SHA-1 Fingerprint (for API key restrictions)

### Option 1: Using Gradle (Recommended)

Open terminal in Android Studio and run:
```bash
# Windows
gradlew signingReport

# Mac/Linux
./gradlew signingReport
```

Look for the SHA1 value under `Variant: debug` section.

### Option 2: Using Keytool

```bash
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
```

## Step 3: Configure API Key Restrictions (Optional but Recommended)

1. Go to **APIs & Services** → **Credentials**
2. Find your API key: `AIzaSyApupOVaHqi5Uo1Ak1k_5hPXnJ-XMuaxvw`
3. Click **Edit**
4. Under **Application restrictions**, select **Android apps**
5. Click **Add an item**
6. Add:
   - **Package name**: `com.harsh.touristguardian`
   - **SHA-1 certificate fingerprint**: (paste your SHA-1 from Step 2)
7. Click **Save**

## Step 4: Verify Setup

1. Wait 1-2 minutes for API to propagate
2. Sync your Android Studio project
3. Build and run the app
4. The map should now load and show your current location!

## Troubleshooting

- **Map not showing**: Check if Maps SDK for Android is enabled
- **API key error**: Verify API key restrictions are set correctly
- **Location not found**: Make sure location permissions are granted and location services are enabled on device

## API Key Location

Your API key is already configured in:
- `app/src/main/AndroidManifest.xml` (line 50)

## Package Name

Your app package name: `com.harsh.touristguardian`


