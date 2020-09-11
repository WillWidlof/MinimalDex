package com.widlof.minimaldex.more

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.widlof.minimaldex.R
import kotlinx.android.synthetic.main.fragment_more.*

class MoreFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_settings.setOnClickListener {
            findNavController().navigate(MoreFragmentDirections.actionMoreFragmentToSettingsFragment())
        }
        tv_other_apps.setOnClickListener {
            navigateToDeveloperPage()
        }
        tv_about.setOnClickListener {
            findNavController().navigate(MoreFragmentDirections.actionMoreFragmentToAboutFragment())
        }
    }

    private fun navigateToDeveloperPage() {
        startActivity(Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(DEVELOPER_STORE_URL)
        })
    }

    companion object {
        private const val DEVELOPER_STORE_URL =
            "https://play.google.com/store/apps/developer?id=Will+Widlof"
    }
}