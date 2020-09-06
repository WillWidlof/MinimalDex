package com.widlof.minimaldex.more

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
            findNavController().navigate(R.id.action_moreFragment_to_settingsFragment)
        }
    }

    companion object {
        fun newInstance(): MoreFragment {
            return MoreFragment()
        }
    }
}