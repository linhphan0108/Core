package com.linhphan.lpcore.ui.forecast.model

/**
 * A generic sealed interface to represent the state of any UI screen.
 * @param T The type of data being loaded (e.g., List<HourlyForecastUiItem>, UserProfile, etc.)
 *
 * @author Linh Phan
 * @since 2025-11-29
 */
sealed interface UiState<out T> {

    /**
     * Represents the initial state or when data has been cleared.
     */
    data object Empty : UiState<Nothing>

    /**
     * Represents the loading state (e.g., spinner visible).
     */
    data object Loading : UiState<Nothing>

    /**
     * Represents the successful loaded state.
     * @param data The actual data to display.
     */
    data class Success<T>(val data: T) : UiState<T>

    /**
     * Represents an error state.
     * @param message A user-friendly error message.
     * @param cause The original exception (optional, helpful for debugging or logging).
     */
    data class Error(val message: String?, val cause: Throwable? = null) : UiState<Nothing>
}
