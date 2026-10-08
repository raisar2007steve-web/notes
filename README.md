# Innovara Notes 📚

**Innovara Notes** is a shared digital bookshelf and living innovation notebook for students. Every student has a personal shelf to document ideas, thoughts, developments, to-do lists, and engineering projects.

---

## 📱 Android APK Build & Testing

This repository includes automated CI/CD for building the Android APK directly on GitHub.

### 1. Automated GitHub Actions Build (One-Click APK Download)
Every time you push or trigger the GitHub Action:
1. Go to the **Actions** tab in your GitHub repository.
2. Click on the latest run under **"Build Android APK"** (or click **"Run workflow"**).
3. Under the **Artifacts** section at the bottom of the summary page, click **`innovara-notes-debug-apk`** to download `innovara-notes-debug.apk`.
4. Transfer the `.apk` file to your Android phone or install it directly via your device browser.

### 2. Building the APK Locally
If you clone this repository to your computer:
```bash
# 1. Install dependencies and build the web bundle
npm install
npm run build

# 2. Build the Android APK using Gradle
./gradlew assembleDebug
```
The output APK will be generated at:
```
app/build/outputs/apk/debug/app-debug.apk
```

### 3. Immediate Testing on a Real Device (PWA)
You can also run Innovara Notes directly on any real phone without installing an APK:
1. Open the [Live Web Application](https://ais-pre-bztagi6wqj6qoltm63lite-822540943868.asia-southeast1.run.app) in mobile Chrome.
2. Tap **`⋮`** → **"Install App"** / **"Add to Home Screen"**.
3. It installs natively on your device with permanent local storage.

---

## 🛠 Tech Stack
- **Frontend:** React 19, TypeScript, Tailwind CSS, Lucide Icons
- **Build Engine:** Vite, Gradle Kotlin DSL (AGP)
- **Native Android:** Kotlin, Jetpack Compose, Hardware-accelerated WebView
- **Persistence:** Local Device Storage (Live, Remembered, Zero Mock Data)
