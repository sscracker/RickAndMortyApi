package ru.example.rickandmortyproject.utils

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class Preferences @Inject constructor(
    private val context: Context
)
 {
      fun getCharacterRepositoryPreferences(): SharedPreferences {
         return context.getSharedPreferences(
             CHARACTERS_PREFERENCES_NAME, Context.MODE_PRIVATE
         )
     }


    companion object{
        private const val CHARACTERS_PREFERENCES_NAME = "charactersRepositoryPreferences"
    }
}