package ru.example.rickandmortyproject.utils

sealed class Response<T : Any> {
    data class Success<T : Any>(val data: T) : Response<T>()
    data class Failure<T : Any>(val error: Throwable) : Response<T>()
}
