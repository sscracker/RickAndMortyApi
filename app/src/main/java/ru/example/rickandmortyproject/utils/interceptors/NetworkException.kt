package ru.example.rickandmortyproject.utils.interceptors

import java.io.IOException

sealed class NetworkException(message: String?) : IOException(message) {

    class ConnectivityException(message: String?) : NetworkException(message)

    class ApiException(message: String?) : NetworkException(message)
}