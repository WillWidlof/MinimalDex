package com.widlof.minimaldex.settings.data

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(context: Context, systemThemeCompat: SystemThemeCompat) {

    private val dataStore: DataStore<Preferences> =
        context.createDataStore(
            name = USER_PREFERENCES_NAME,
            // Since we're migrating from SharedPreferences, add a migration based on the
            // SharedPreferences name
        )

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
        //some error
    }.map {preferences ->
            val isSystemTheme = preferences[PreferenceKeys.SYSTEM_THEME_KEY] ?: true
            val isDarkTheme = preferences[PreferenceKeys.DARK_THEME_KEY] ?: systemThemeCompat.isDarkThemeEnabled()
            UserPreferences(isSystemTheme, isDarkTheme)
        }

    suspend fun updateIsSystemThemeEnabled(isSystemThemeEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.SYSTEM_THEME_KEY] = isSystemThemeEnabled
        }
    }

    suspend fun updateIsDarkThemeEnabled(isDarkThemeEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.DARK_THEME_KEY] = isDarkThemeEnabled
        }
    }


    data class UserPreferences(
        val isSystemTheme: Boolean,
        val isDarkTheme: Boolean
    )

    private object PreferenceKeys {
        val SYSTEM_THEME_KEY = preferencesKey<Boolean>("system_theme")
        val DARK_THEME_KEY = preferencesKey<Boolean>("dark_theme")
    }

    companion object {
        private const val USER_PREFERENCES_NAME = "user_preferences"
    }
}