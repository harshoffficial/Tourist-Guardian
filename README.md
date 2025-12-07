# Tourist Guardian - 

<div align="center">

![Tourist Guardian](app/src/main/logo-playstore.png)

**A comprehensive Android application designed to keep travelers safe while exploring India by providing real-time location tracking, danger zone alerts, AI-powered travel planning, and emergency assistance features.**

[![Android](https://img.shields.io/badge/Android-24%2B-green.svg)](https://www.android.com/)
[![Kotlin](https://img.shields.io/badge/Language-Java-orange.svg)](https://www.java.com/)
[![Firebase](https://img.shields.io/badge/Firebase-Enabled-yellow.svg)](https://firebase.google.com/)

</div>

---

## 🎯 Overview

**Tourist Guardian** is a safety-focused Android application specifically designed for travelers exploring India. The app combines real-time GPS tracking, comprehensive danger zone mapping, AI-powered travel planning, and emergency assistance features to ensure tourists have a safe and informed travel experience.

The application addresses critical safety concerns by:
- **Visualizing danger zones** on an interactive map with animated alerts
- **Providing AI-generated travel itineraries** tailored to specific destinations
- **Offering instant SOS emergency assistance** with location sharing
- **Tracking real-time location** with high-accuracy GPS
- **Alerting users** about wildlife conflict areas, crime hotspots, and landslide-prone regions

---

## ✨ Features

### 🗺️ Interactive Map with Real-Time Location

- **Full-screen immersive map experience** using Leaflet.js and OpenStreetMap
- **Real-time GPS tracking** with high-accuracy location services
- **Automatic map centering** on user's current location
- **Zoom controls** and multi-touch gestures for easy navigation
- **Current location marker** with coordinates display
- **Offline map support** (tiles cached for offline viewing)

### ⚠️ Danger Zone Visualization

The app displays three categories of danger zones with animated, pulsing circles:

#### 🦁 Wildlife Conflict Areas (15 Locations)
- **Kaziranga National Park** - Rhino/Elephant/Tiger conflicts
- **Tadoba–Andhari** - Tiger attack hotspot
- **Sundarbans** - Man-tiger conflict zones
- **Bandipur/Nilgiri** - Elephant & carnivore movement areas
- **Nagarhole (Kabini)** - Tourist/edge conflict zones
- **Jim Corbett National Park** - Core + buffer villages
- **Ranthambore** - Park core + fringe farmland
- **Periyar** - Hilly forest + plantations
- **Manas National Park** - Transboundary conflict zones
- **Kanha** - Buffer farmland interface
- **Dudhwa/Terai** - Elephant/tiger incidents
- **Simlipal** - Reserve + fringe villages
- **Nameri/Sonai Rupai** - Elephant/tiger movement
- **Buxa/Dooars** - Tea garden/forest edges
- **Kabini–Bangalore fringe** - Corridor/settlement clashes

#### 🚨 Crime Hotspots (15 Cities)
- **Kochi** - Highest crime-rate metro
- **Delhi (NCT)** - Large metro high crime area
- **Surat, Jaipur, Patna, Indore, Lucknow** - High crime rate cities
- **Nagpur** - Violent-crime surge area
- **Kozhikode, Ahmedabad** - High crime rate zones
- **Faridabad, Ludhiana** - Murder hotspots
- **Asansol, Agra, Gwalior** - High crime rate areas

#### 🏔️ Landslide-Prone Areas (10 Locations)
- **Rudraprayag** - Most landslide-prone
- **Tehri Garhwal** - Landslide-prone region
- **Chamoli/Rishiganga** - Valley instability
- **Sikkim (Gangtok)** - Steep terrain
- **Mizoram (Aizawl)** - High landslide counts
- **Idukki/Western Ghats** - Hilly plantations
- **Nilgiris/Kodaikanal** - Western Ghats region
- **Arunachal Himalayan** - Monsoon-triggered slides
- **Darjeeling** - Hills landslide-prone
- **Tripura (Agartala)** - High landslide counts

**Visual Features:**
- **Animated pulsing circles** that blink and expand
- **Fade-out effect** with multiple concentric layers
- **Color-coded markers** (Red for wildlife, Dark red for crime, Brown for landslides)
- **Toggle buttons** to show/hide each category
- **Interactive popups** with detailed information about each zone
- **Radius indicators** showing the danger zone coverage area

### 🤖 AI Travel Planner

Powered by **Groq AI** (using Llama 3.3 70B model), the AI Travel Planner generates comprehensive, personalized travel itineraries:

- **Input Requirements:**
  - Destination (e.g., Goa, Rajasthan, Kerala)
  - Number of days (1-30 days)

- **Generated Itinerary Includes:**
  - Day-by-day schedule with specific places to visit
  - Best time to visit each location
  - Estimated travel time between locations
  - Safety tips specific to the destination
  - Local cuisine recommendations
  - Budget estimates (budget, mid-range, luxury options)
  - Important cultural notes and customs
  - Emergency contacts for the destination

- **Features:**
  - Beautiful animated background with Lottie animations
  - Clean, readable result display in a white card
  - Share functionality to send plans via WhatsApp, SMS, or email
  - Formatted output with clear sections and bullet points

### 🆘 SOS Emergency Feature

- **One-tap emergency assistance:**
  - Automatically dials emergency number (112 in India)
  - Shares location via SMS/WhatsApp with Google Maps link
  - Includes coordinates (latitude/longitude)
  - Sends emergency message to contacts

- **Location Sharing:**
  - Share current location with friends and family
  - Includes Google Maps link for easy navigation
  - Coordinates displayed for manual entry if needed

### 🎨 User Interface

- **Full-screen immersive experience** - No action bar or status bar
- **Modern Material Design** with smooth animations
- **Slide-in navigation drawer** from the right side
- **Professional legend box** with toggle controls
- **Responsive layout** that adapts to different screen sizes
- **Dark/light theme support** (system-based)

### 🔐 Authentication & Security

- **Firebase Authentication:**
  - Email/password login
  - Secure sign-up with username
  - Session management
  - Auto-logout on session expiry

- **Data Security:**
  - User data stored securely in Firebase Realtime Database
  - Encrypted API communications
  - Secure API key storage

---

## 🛠️ Technology Stack

### Frontend
- **Language:** Java
- **UI Framework:** Android SDK with Material Design Components
- **Maps:** Leaflet.js (OpenStreetMap) via WebView
- **Animations:** Lottie for smooth animations
- **Layout:** ConstraintLayout, LinearLayout

### Backend & Services
- **Authentication:** Firebase Authentication
- **Database:** Firebase Realtime Database
- **Location Services:** Google Play Services Location API
- **AI Integration:** Groq API (Llama 3.3 70B model)
- **HTTP Client:** OkHttp3

### Libraries & Dependencies
- Firebase Auth
- Firebase Realtime Database
- Firebase Storage
- Google Play Services Location
- Material Design Components
- Lottie Animations
- OkHttp3
- AndroidX Libraries---

## 📦 Installation & Setup

### Prerequisites

- **Android Studio** (Hedgehog or later recommended)
- **JDK 11** or higher
- **Android SDK** (API Level 24+)
- **Firebase Account** (for authentication and database)
- **Groq API Key** (for AI Travel Planner)
- **Internet Connection** (for maps and API calls)

### Step 1: Clone the Repository
h
git clone https://github.com/yourusername/TouristGuardian.git
cd TouristGuardian### Step 2: Firebase Setup

1. **Create Firebase Project:**
   - Go to [Firebase Console](https://console.firebase.google.com/)
   - Create a new project named "TouristGuardian"
   - Enable Authentication (Email/Password)
   - Enable Realtime Database
   - Enable Storage (if needed)

2. **Add Android App:**
   - Click "Add app" → Android
   - Package name: `com.harsh.touristguardian`
   - Download `google-services.json`
   - Place it in `app/` directory

3. **Configure Database:**
   - Go to Realtime Database
   - Set up security rules (see `database.rules.json`)
   - Enable read/write access for authenticated users

### Step 3: Configure API Keys

1. **Groq API Key:**
   - Open `app/src/main/java/com/harsh/touristguardian/AITravelPlannerActivity.java`
   - Replace `GROQ_API_KEY` with your Groq API key:a
   private static final String GROQ_API_KEY = "your_groq_api_key_here";
      - Get your API key from [Groq Console](https://console.groq.com/)

2. **Location Services:**
   - Already configured via Google Play Services
   - No additional setup needed

### Step 4: Build and Run

1. **Open Project:**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory

2. **Sync Gradle:**
   - Click "Sync Now" when prompted
   - Wait for dependencies to download

3. **Run on Device/Emulator:**
   - Connect Android device (API 24+) or start emulator
   - Click "Run" button or press `Shift + F10`
   - Grant location permissions when prompted

### Step 5: First Run

1. **Sign Up:**
   - Create a new account with email and password
   - Enter a username
   - Complete registration

2. **Grant Permissions:**
   - Allow location access (required for map features)
   - Allow internet access (required for maps and AI)

3. **Explore Features:**
   - View your current location on the map
   - Toggle danger zones to see alerts
   - Try the AI Travel Planner
   - Test SOS emergency feature

---
