# Core Android Project

This is a robust core structure for an Android application built with **Kotlin**, following **Clean Architecture** and the **MVVM (Model-View-ViewModel)** pattern, utilizing modern Android development best practices.

## 🛠 Tech Stack

*   **Language**: [Kotlin](https://kotlinlang.org/)
*   **Architecture**: Clean Architecture + MVVM
*   **Dependency Injection**: [Dagger Hilt](https://dagger.dev/hilt/)
*   **Asynchronous Programming**: [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html)
*   **Network**: [Retrofit](https://square.github.io/retrofit/) with Gson
*   **UI**: XML Layouts with [ViewBinding](https://developer.android.com/topic/libraries/view-binding)
*   **Lifecycle**: [AndroidX Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle)
*   **Logging**: [Timber](https://github.com/JakeWharton/timber)

## 📂 Project Structure

The project is organized by layers to enforce separation of concerns:

```text
com.linhphan.lpcore
├── di/                 # Dependency Injection modules (AppModule)
├── domain/             # Domain Layer (Business Logic)
│   ├── base/           # BaseUseCase
│   ├── model/          # Domain Models
│   ├── usecase/        # Use Cases (e.g., GetForecastUseCase)
│   └── mapper/         # Data to Domain Mappers
├── data/               # Data Layer (Repository Implementation & Data Sources)
│   ├── forecast/       # Feature specific data (Repository, Remote Service, Models)
│   └── Result.kt       # State wrapper (Success, Error, Loading)
├── ui/                 # Presentation Layer
│   ├── base/           
│   │   ├── activity/   # BaseActivity, BaseActivityViewModel
│   │   └── fragment/   # BaseFragment, BaseFragmentActivityViewModel
│   ├── main/           # MainActivity
│   └── twosidepannels/ # TwoSideScreenActivity and its fragments
└── CoreApplication.kt  # Application class setup for Hilt
```

### Key Components

*   **Domain Layer**: Contains pure Kotlin logic.
    *   **`BaseUseCase`**: Abstract base class for UseCases, handling coroutine context switching (usually to IO).
    *   **`GetForecastUseCase`**: Example of a use case encapsulating specific business logic.
*   **Data Layer**: Handles data retrieval.
    *   **`ForecastRepository`**: Repository interface and implementation to manage data sources.
    *   **`Result<T>`**: Sealed class to handle data states (`Success`, `Error`, `Loading`).
*   **UI Layer**:
    *   **`BaseActivity` & `BaseFragment`**: Abstract classes that handle `ViewBinding` inflation and standard setup.
    *   **`BaseActivityViewModel`**: Abstract ViewModel containing common state management.

## ✅ Testing

The project is configured for easy testing of logic, repositories, and UI.

### Unit Tests
Located in `src/test/java`.
*   Frameworks: JUnit 4, MockK, kotlinx-coroutines-test.
*   **MainDispatcherRule**: A custom rule to swap the Main dispatcher with a test dispatcher.
*   **ViewModel Tests**: Testing ViewModels with simulated delays and flow states.

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
