package com.rigobertods.rdscore.core.common

sealed interface UiState<out T> {
    val data: T?

    data class Loading<T>(override val data: T? = null) : UiState<T>
    data class Success<T>(override val data: T) : UiState<T>
    data class Error<T>(val message: String, override val data: T? = null) : UiState<T>
}
