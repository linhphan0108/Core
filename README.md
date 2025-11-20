# Core Android Project

This is a robust core structure for an Android application built with **Kotlin**, following the **MVVM (Model-View-ViewModel)** architecture and modern Android development best practices.

## 🛠 Tech Stack

*   **Language**: [Kotlin](https://kotlinlang.org/)
*   **Architecture**: MVVM (Model-View-ViewModel)
*   **Dependency Injection**: [Dagger Hilt](https://dagger.dev/hilt/)
*   **Asynchronous Programming**: [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html)
*   **UI**: XML Layouts with [ViewBinding](https://developer.android.com/topic/libraries/view-binding)
*   **Lifecycle**: [AndroidX Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle)

## 📂 Project Structure

The project is organized by feature/layer to maintain separation of concerns:

```text
com.linhphan.lpcore
├── di/                 # Dependency Injection modules (AppModule)
├── ui/
│   ├── base/           # Base classes (BaseActivity, BaseViewModel)
│   ├── main/           # Main feature (MainActivity, MainViewModel)
│   └── twosidepannels/ # Two-pane layout feature
├── data/               # Data models and State wrappers (Result)
└── CoreApplication.kt  # Application class setup for Hilt
```

### Key Components

*   **`BaseActivity`**: Abstract class that handles `ViewBinding` inflation and standard setup (`setupViews`, `setupObservers`).
*   **`BaseViewModel`**: Abstract ViewModel containing common state management for loading (`isLoading`) and error (`error`) handling.
*   **`Result<T>`**: Sealed class to handle data states (`Success`, `Error`, `Loading`).
*   **`AppModule`**: Hilt module providing Coroutine Dispatchers (`IoDispatcher`, `MainDispatcher`, `DefaultDispatcher`) for better testability.

## ✅ Testing

The project is configured for easy testing of both logic and UI.

### Unit Tests
Located in `src/test/java`.
*   Frameworks: JUnit 4, MockK, kotlinx-coroutines-test.
*   Example: `MainViewModelTest` demonstrates how to test ViewModels using a test coroutine dispatcher.
*   **PanelOneFragmentViewModelTest**: Demonstrates testing ViewModels with simulated delays and flow states.

### UI / Instrumented Tests
Located in `src/androidTest/java`.
*   Frameworks: Espresso, Espresso Intents, Hilt Testing.
*   **Custom Runner**: `CustomTestRunner` is configured to support Hilt injection in tests.
*   **HiltTestActivity**: A specialized activity to host fragments in isolation during tests.
*   **launchFragmentInHiltContainer**: Helper function to launch fragments with Hilt support.
*   **Example**: `MainActivityTest` validates UI elements and navigation intents. `PanelOneFragmentTest` verifies fragment UI in isolation.

## 🚀 Getting Started

1.  **Open** the project in Android Studio.
2.  Let Gradle **sync** to download dependencies.
3.  **Run** the app (`Run > Run 'app'`).

### Running Tests
*   **Unit Tests**: Right-click `src/test/java` -> Run 'Tests in ...'
*   **UI Tests**: Right-click `src/androidTest/java` -> Run 'Tests in ...'
