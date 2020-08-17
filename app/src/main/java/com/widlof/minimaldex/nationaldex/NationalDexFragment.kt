package com.widlof.minimaldex.nationaldex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.widlof.minimaldex.R
import com.widlof.minimaldex.nationaldex.data.model.PokemonListSingle
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * A placeholder fragment containing a simple view.
 */
class NationalDexFragment : Fragment() {

    private lateinit var nationalDexViewModel: NationalDexViewModel
    private lateinit var nationalDexAdapter: NationalDexAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nationalDexViewModel =
            ViewModelProvider(this).get(NationalDexViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nationalDexViewModel.pokemonList.observe(viewLifecycleOwner, Observer {
            nationalDexAdapter.apply {
                pokemonList = it
                notifyDataSetChanged()
            }
        })
        setUpRecycler()
        nationalDexViewModel.getPokemonList()
    }

    private fun setUpRecycler() {
        rv_national_dex.apply {
            layoutManager = LinearLayoutManager(requireContext())
            nationalDexAdapter = NationalDexAdapter(requireContext(), mutableListOf(), object: NationalDexListener {
                override fun onPokemonClicked(pokemon: PokemonListSingle) {
                    Toast.makeText(activity, pokemon.name, Toast.LENGTH_LONG).show()
                }

            })
            adapter = nationalDexAdapter
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): NationalDexFragment {
            return NationalDexFragment()
        }
    }
}