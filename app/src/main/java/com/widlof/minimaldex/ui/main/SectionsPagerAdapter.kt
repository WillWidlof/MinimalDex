package com.widlof.minimaldex.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.widlof.minimaldex.R
import com.widlof.minimaldex.items.ItemListFragment
import com.widlof.minimaldex.nationaldex.NationalDexFragment

private val TAB_TITLES = arrayOf(
        R.string.tab_home,
        R.string.tab_items,
        R.string.tab_settings
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager)
    : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return when(position) {
            0 -> NationalDexFragment.newInstance()
            1 -> ItemListFragment.newInstance()
            else -> ItemListFragment.newInstance()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 3
    }
}