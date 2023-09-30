package ru.example.rickandmortyproject.utils.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class NetworkExceptionInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return try {
            val request = chain.request()
            val response = chain.proceed(request)
            throwExceptionWhenResponseIsNotSuccessful(response)
            response
        } catch (e: Exception) {
            when (e) {
                is NetworkException.ConnectivityException -> {
                    throw NetworkException.ConnectivityException(e.message)
                }
                else -> {
                    throw NetworkException.ApiException(e.message)
                }
            }
        }
    }
}

private fun throwExceptionWhenResponseIsNotSuccessful(response: Response) {
    if (!response.isSuccessful) {
        throw NetworkException.ApiException(response.message)
    }
}
