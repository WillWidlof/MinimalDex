@file:Suppress("UNCHECKED_CAST")

package com.widlof.minimaldex.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.widlof.minimaldex.settings.data.SystemThemeCompat
import com.widlof.minimaldex.settings.data.UserPreferencesRepository
import java.lang.ClassCastException

class SettingsViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            val systemThemeCompat = SystemThemeCompat(context.resources.configuration)
            val repository = UserPreferencesRepository(context = context, systemThemeCompat = systemThemeCompat)
            SettingsViewModel(repository, systemThemeCompat) as T
        } else {
            throw ClassCastException("No matching view model found")
        }
    }
}