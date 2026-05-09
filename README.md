# Medication Tracker

An offline-first Android application designed to help users manage their medications, schedule reliable exact-time reminders, and keep a historical log of their taken doses. 

This project demonstrates modern Android architecture using Jetpack Compose, MVVM, Room (with relational schemas), Coroutines/Flow, and Dagger Hilt for dependency injection.

## Features

* **Medication Management:** Add and view your medications, including dosage amounts and scheduled times.
* **Exact Reminders:** Leverages `AlarmManager` to provide exact-time notifications when it is time to take a medication, even if the device is idle or dozing.
* **Actionable Notifications:** Respond to reminders directly from the notification tray with dedicated **Take** and **Snooze** actions.
* **Dose History:** Automatically logs exactly when a medication was taken, keeping a clear historical record for your peace of mind.
* **Offline-First:** All data is stored locally on the device using a Room SQLite database.

## Tech Stack

This project uses modern Android development practices and libraries:
* **UI:** Jetpack Compose (Material 3)
* **Architecture:** MVVM (Model-View-ViewModel)
* **Database:** Room (SQLite) with relational `@Embedded` mapping
* **Dependency Injection:** Dagger Hilt
* **Concurrency:** Kotlin Coroutines & Flow
* **Background Processing:** `AlarmManager` and `BroadcastReceiver`s for scheduling and action handling.

## Requirements

* Android Studio (latest stable version recommended)
* Minimum SDK: 26 (Android 8.0)
* Target SDK: 35 (Android 15)

## Getting Started

1. **Clone the repository:**
   ```bash
   git clone <your-repository-url>
   ```
2. **Open the project** in Android Studio.
3. **Sync Project with Gradle Files** to download the necessary dependencies (Compose, Room, Hilt, etc.).
4. **Hilt Setup:** Ensure your project has an `Application` class annotated with `@HiltAndroidApp` and declared in your `AndroidManifest.xml` (e.g., `<application android:name=".MyApplication" ...>`).
5. **Run the App** on an Android emulator or a physical device.

## Project Structure

* **`ui/MedicationViewModel.kt`**: Exposes `StateFlow`s for Jetpack Compose UI to observe medication lists and relational dose histories.
* **`ui/MedicationDao.kt`**: The Room Data Access Object handling CRUD operations and `@Transaction` mapping for embedded relations.
* **`ui/AlarmScheduler.kt`**: A utility class utilizing `AlarmManager` to schedule `PendingIntent`s for the exact medication time.
* **`ui/MedicationReminderReceiver.kt`**: An `@AndroidEntryPoint` BroadcastReceiver that triggers notifications, logs taken doses using the injected DAO, and processes Snooze loop logic.

### Important Notes on Permissions

If you are running this app on a device with Android 13 (API 33) or higher, you will be prompted to grant **Notification** permissions. For devices running Android 12 (API 31) or higher, exact alarms require the **Alarms & Reminders** permission. Ensure these are granted to receive medication reminders on time.

These can be requested at runtime using Jetpack Compose's `rememberLauncherForActivityResult`.

## Future Enhancements

* Add UI for users to create new medications.
* Allow editing and deleting existing medications.
* Dynamic snooze interval configuration.