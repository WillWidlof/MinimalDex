package com.widlof.minimaldex.settings.data

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

class SystemThemeCompat(private val configuration: Configuration) {

    fun isDarkThemeEnabled() =
        when (configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_YES -> true
        Configuration.UI_MODE_NIGHT_NO -> false
        else -> false
    }

    fun setSystemThemeEnabled() =
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

    fun setDarkThemeEnabled() = AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

    fun setLightThemeEnabled() = AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

}