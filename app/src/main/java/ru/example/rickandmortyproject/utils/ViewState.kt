package ru.example.rickandmortyproject.utils

sealed class ViewState<out T: Any> {
    data object Loading: ViewState<Nothing>()

    data class Data<out T: Any>(val data: T): ViewState<T>()

    data class Error(val error: Throwable): ViewState<Nothing>()
}