package ru.example.rickandmortyproject.utils

import android.content.Context
import android.widget.Toast
import com.google.gson.JsonArray

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun JsonArray.getIdListFromUrls() = toList().map { url ->
    url.asString.split("/").last().toInt()
}