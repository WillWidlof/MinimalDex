package com.widlof.minimaldex.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.widlof.minimaldex.settings.data.SystemThemeCompat
import com.widlof.minimaldex.settings.data.UserPreferencesRepository
import kotlinx.coroutines.launch

class SettingsViewModel(private val userPreferencesRepository: UserPreferencesRepository,
                        private val systemThemeCompat: SystemThemeCompat): ViewModel() {

    fun systemThemeToggled(isEnabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateIsSystemThemeEnabled(isEnabled)
            if (isEnabled) {
                systemThemeCompat.setSystemThemeEnabled()
            } else {
                if (userPreferences.value?.isDarkTheme == true) {
                    systemThemeCompat.setDarkThemeEnabled()
                } else {
                    systemThemeCompat.setLightThemeEnabled()
                }
            }
        }
    }

    fun darkThemeToggled(isEnabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateIsDarkThemeEnabled(isEnabled)
            if (isEnabled) {
                systemThemeCompat.setDarkThemeEnabled()
            } else {
                systemThemeCompat.setLightThemeEnabled()
            }
        }
    }

    val userPreferences = userPreferencesRepository.userPreferencesFlow.asLiveData()
}