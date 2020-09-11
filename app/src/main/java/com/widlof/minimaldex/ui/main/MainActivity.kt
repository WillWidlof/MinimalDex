package com.widlof.minimaldex.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.widlof.minimaldex.R
import com.widlof.minimaldex.settings.data.SystemThemeCompat
import com.widlof.minimaldex.settings.data.UserPreferencesRepository
import com.widlof.minimaldex.util.ViewUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        app_bar.elevation = 0f

        val systemThemeCompat = SystemThemeCompat(resources.configuration)
        val userPreferencesRepository = UserPreferencesRepository(this, systemThemeCompat)
        val data = userPreferencesRepository.userPreferencesFlow.take(1).asLiveData().observe(this, Observer {
            if (it.isSystemTheme) {
                systemThemeCompat.setSystemThemeEnabled()
            } else {
                if (it.isDarkTheme) {
                    systemThemeCompat.setDarkThemeEnabled()
                } else {
                    systemThemeCompat.setLightThemeEnabled()
                }
            }
        })
        }
    }