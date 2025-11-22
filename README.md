# Core Android Project

This is a robust core structure for an Android application built with **Kotlin**, following **Clean Architecture** and the **MVVM (Model-View-ViewModel)** pattern, utilizing modern Android development best practices and the **Single Source of Truth (SSOT)** principle.

## 🛠 Tech Stack

*   **Language**: [Kotlin](https://kotlinlang.org/)
*   **Architecture**: Clean Architecture + MVVM + Repository Pattern (SSOT)
*   **Dependency Injection**: [Dagger Hilt](https://dagger.dev/hilt/)
*   **Asynchronous Programming**: [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html)
*   **Network**: [Retrofit](https://square.github.io/retrofit/) with Gson
*   **Local Storage**: [Room Database](https://developer.android.com/training/data-storage/room)
*   **UI**: XML Layouts with [ViewBinding](https://developer.android.com/topic/libraries/view-binding)
*   **Lifecycle**: [AndroidX Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle)
*   **Logging**: [Timber](https://github.com/JakeWharton/timber)

## 🏗 High-Level Architecture

![Architecture Diagram](architecture.png)

> **Note**: You can view the Mermaid diagram source code in [ARCHITECTURE.md](ARCHITECTURE.md) or generate an updated image using the [Mermaid Live Editor](https://mermaid.live/).

## 📂 Project Structure

The project is organized by layers to enforce separation of concerns:

```text
com.linhphan.lpcore
├── di/                 # Dependency Injection modules (AppModule, RepositoryModule, etc.)
├── domain/             # Domain Layer (Pure Kotlin Business Logic)
│   ├── base/           # Base classes (BaseFlowUseCase, BaseSuspendUseCase, Result<T>)
│   ├── model/          # Domain Models (e.g., Cake, Forecasts)
│   ├── repository/     # Repository Interfaces
│   └── usecase/        # Use Cases (e.g., GetForecastUseCase, RefreshForecastUseCase)
├── data/               # Data Layer (Repository Implementation & Data Sources)
│   └── forecast/       # Forecast feature data
│       ├── local/      # Room Database (Dao, Entities, LocalDataSource)
│       ├── remote/     # Retrofit Service (RemoteDataSource)
│       └── ForecastRepositoryImpl.kt # Repository implementation coordinating Local & Remote
├── ui/                 # Presentation Layer
│   ├── base/           
│   │   ├── activity/   # BaseActivity, BaseActivityViewModel
│   │   └── fragment/   # BaseFragment, BaseFragmentActivityViewModel
│   ├── main/           # MainActivity
│   ├── forecast/       # Forecast Feature UI (ViewModel, Activity, Adapter)
│   └── twosidepannels/ # TwoSideScreenActivity and its fragments
└── CoreApplication.kt  # Application class setup for Hilt
```

### Key Components

*   **Domain Layer**: The heart of the application, independent of Android frameworks.
    *   **`BaseFlowUseCase` & `BaseSuspendUseCase`**: Abstract base classes for UseCases, standardized for `Flow` streams or `suspend` calls.
    *   **`Result<T>`**: Sealed class to handle data states (`Success`, `Error`, `Loading`).
*   **Data Layer**: Implements the **Single Source of Truth** pattern.
    *   **`ForecastRepositoryImpl`**: 
        *   Exposes a `Flow` of data from the **Local Data Source** (Room).
        *   Fetches data from the **Remote Data Source** (Retrofit) and saves it to Local.
        *   The UI observes the Local data flow, ensuring it always displays the latest persisted state.
*   **UI Layer**: Handles User Interface and State.
    *   **`ForecastActivityViewModel`**: Uses `SavedStateHandle` to persist search coordinates. Triggers data refreshes and observes the domain UseCases.

## ✅ Testing

The project is configured for easy testing of logic, repositories, and UI.

### Unit Tests
Located in `src/test/java`.
*   Frameworks: JUnit 4, MockK, kotlinx-coroutines-test.
*   **MainDispatcherRule**: A custom rule to swap the Main dispatcher with a test dispatcher.
*   **ViewModel Tests**: Testing ViewModels with simulated delays and flow states using `UnconfinedTestDispatcher`.

### UI / Instrumented Tests
Located in `src/androidTest/java`.
*   Frameworks: Espresso, Espresso Intents, Hilt Testing.
*   **CustomTestRunner**: Configured to support Hilt injection in tests.
*   **HiltTestActivity**: A specialized activity to host fragments in isolation during tests.
*   **TestAppModule**: Provides test dispatchers (Unconfined) for synchronous execution in UI tests.

## 🚀 Getting Started

1.  **Open** the project in Android Studio.
2.  Let Gradle **sync** to download dependencies.
3.  **Run** the app (`Run > Run 'app'`).
