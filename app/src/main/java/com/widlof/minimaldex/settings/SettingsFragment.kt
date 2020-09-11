package com.widlof.minimaldex.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.DataStore
import androidx.datastore.preferences.PreferenceDataStoreFactory
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.widlof.minimaldex.R
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.File

class SettingsFragment : Fragment() {

    private lateinit var viewMdel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val ui = inflater.inflate(R.layout.fragment_settings, container, false)
        viewMdel = ViewModelProvider(this, SettingsViewModelFactory(requireContext()))
            .get(SettingsViewModel::class.java)
        initialiseUI(ui)
        return ui
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    findNavController().apply {
                        navigateUp()
                        popBackStack(R.id.action_moreFragment_to_settingsFragment, true)
                    }
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }


    private fun initialiseUI(it: View) {
        viewMdel.userPreferences.observe(viewLifecycleOwner, Observer {
            sw_sys_theme.isChecked = it.isSystemTheme
            sw_dark_theme.isChecked = it.isDarkTheme
            sw_dark_theme.isEnabled = !it.isSystemTheme
        })
        addSystemThemeListener(it)
    }

    private fun addSystemThemeListener(view: View) {
        with(view) {
            sw_sys_theme.setOnCheckedChangeListener { buttonView, isChecked ->
                viewMdel.systemThemeToggled(isChecked)
            }
            sw_dark_theme.setOnCheckedChangeListener { buttonView, isChecked ->
                viewMdel.darkThemeToggled(isChecked)
            }
        }
    }

    private fun setThemeManually(isDarkTheme: Boolean) {
//        MainScope().launch {
//            preferences.edit { settings ->
//                //settings[DARK_THEME_KEY] = isDarkTheme
//            }
//        }
        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }


    companion object {

        private const val SETTINGS = "settings"

    }
}