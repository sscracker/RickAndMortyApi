package ru.example.rickandmortyproject.utils

sealed class ResponseUtil<T : Any> {
    data class Success<T : Any>(val data: T) : ResponseUtil<T>()
    data class Failure<T : Any>(val error: Throwable) : ResponseUtil<T>()
}
