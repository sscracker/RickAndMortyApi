package ru.example.rickandmortyproject.utils.interceptors

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager

class AppChuckerInterceptor(
    private val context: Context
) {

    fun intercept() = ChuckerInterceptor.Builder(context)
        .collector(chuckerCollector())
        .maxContentLength(250_000L)
        .alwaysReadResponseBody(true)
        .build()

    private fun chuckerCollector() = ChuckerCollector(
        context = context,
        showNotification = true,
        retentionPeriod = RetentionManager.Period.ONE_WEEK
    )
}