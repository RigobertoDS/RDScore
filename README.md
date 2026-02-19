<p align="center">
  <img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.webp" width="120" alt="RDScore Logo"/>
</p>

<h1 align="center">⚽ RDScore</h1>

<p align="center">
  <b>Football Match Predictions & Statistical Analysis</b><br/>
  <i>Powered by Machine Learning Models</i>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white" alt="Platform"/>
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?logo=kotlin&logoColor=white" alt="Language"/>
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?logo=jetpackcompose&logoColor=white" alt="UI"/>
  <img src="https://img.shields.io/badge/Min%20SDK-29-brightgreen" alt="Min SDK"/>
  <img src="https://img.shields.io/badge/Target%20SDK-36-blue" alt="Target SDK"/>
</p>

---

## 📖 About

**RDScore** is a native Android application that provides **AI-driven football match predictions** and in-depth statistical analysis. It connects to a proprietary backend that runs machine learning models to generate predictions for upcoming matches across multiple European leagues.

> ⚠️ **Disclaimer:** RDScore is a statistical analysis tool. It does not allow betting and is not affiliated with any bookmaker. All predictions are based on mathematical models and historical data — they do not guarantee results.

---

## ✨ Features

### 🔮 Match Predictions
- **Three prediction models**: Conservative, Moderate, and Aggressive — each with different risk profiles
- **Three markets**: 
  - 🏆 **Result (1X2)** — Win/Draw/Loss probabilities
  - ⚽ **Both Teams to Score (BTTS)** — Yes/No analysis
  - 📊 **Over/Under 2.5 Goals** — Statistical likelihood
- **Expected goals** calculation for each team
- **Recommendation system**: Bet / Don't bet signals per model

### 🔥 Hot Odds
- Identifies **high-value statistical opportunities** across all matches
- Highlights picks where the model finds significant edges

### 📈 Model Statistics & Summary
- Track model **accuracy and ROI** over time
- Per-market and per-model precision breakdowns
- Total matches analyzed, hit rates, and performance summaries

### 🏟️ Team & League Details
- Full team statistics: record (W-D-L), goals for/against, form, position
- Home vs away performance splits
- League standings with full table view
- Recent match history per team

### 👤 User System
- Account registration, login, and profile management
- Password recovery via email
- Profile editing and account deletion

### 🌍 Multi-language Support
Available in **5 languages**:

| 🇪🇸 Español | 🇬🇧 English | 🇫🇷 Français | 🇩🇪 Deutsch | 🇮🇹 Italiano |
|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ | ✅ | ✅ | ✅ |

### 🎨 Theming
- Light and Dark mode support
- Dynamic theme switching from the app

---

## 🏗️ Tech Stack

| Layer | Technology |
|---|---|
| **Language** | Kotlin |
| **UI** | Jetpack Compose + Material 3 |
| **Architecture** | MVVM + Clean Architecture |
| **DI** | Hilt (Dagger) |
| **Networking** | Retrofit + OkHttp |
| **Local Storage** | Room Database + DataStore |
| **Async** | Kotlin Coroutines + Flow |
| **Navigation** | Jetpack Navigation Compose |
| **Image Loading** | Coil |

---

## 📁 Project Structure

```
app/src/main/java/com/rigobertods/rdscore/
├── core/
│   ├── common/         # Language, Theme managers, UiState
│   ├── data/           # Room DB, DAOs, Entities, Mappers
│   ├── network/        # Retrofit ApiService, Auth interceptors
│   ├── ui/             # Theme, Colors, Typography
│   └── util/           # Locale helpers
├── data/               # Session management, Error handling
├── di/                 # Hilt modules (App, Database, Network, Repository)
├── features/
│   ├── auth/           # Login, Register, Password recovery
│   ├── cuotascalientes/# Hot Odds feature
│   ├── equipo/         # Team details
│   ├── ligas/          # League data
│   ├── partidos/       # Matches, predictions, match details
│   ├── perfil/         # User profile management
│   └── resumen/        # Model accuracy & summary stats
└── ui/
    ├── components/     # Reusable Compose components
    ├── navigation/     # Nav routes
    └── util/           # Date utils, message mappers
```

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio** Ladybug or newer
- **JDK 11+**
- **Android SDK 36**

### Build & Run

```bash
# Clone the repository
git clone https://github.com/RigobertoDS/RDScore.git

# Open in Android Studio and sync Gradle
# Run on an emulator or physical device (API 29+)
```

---

## 🔗 Backend

RDScore connects to a proprietary REST API hosted at [rdscore.com](https://www.rdscore.com) that provides:
- Match data and predictions from ML models
- User authentication (JWT-based with token refresh)
- Team and league statistical data
- Hot odds calculations

---

## 📄 License

This project is open-source for educational and portfolio purposes.  
All rights reserved © RigobertoDS.

---

<p align="center">
  <b>Built with ❤️ by <a href="https://github.com/RigobertoDS">RigobertoDS</a></b>
</p>
