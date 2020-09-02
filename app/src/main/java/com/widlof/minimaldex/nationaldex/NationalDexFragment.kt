package com.widlof.minimaldex.nationaldex

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.widlof.minimaldex.R
import com.widlof.minimaldex.nationaldex.data.model.PokemonListSingle
import com.widlof.minimaldex.util.ViewUtils
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.*

class NationalDexFragment : Fragment() {

    private lateinit var nationalDexViewModel: NationalDexViewModel
    private lateinit var nationalDexAdapter: NationalDexAdapter
    private var baseList: List<PokemonListSingle> = listOf()
    private var isMenuOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nationalDexViewModel =
            ViewModelProvider(
                this,
                NationalDexViewModelFactory()
            ).get(NationalDexViewModel::class.java)
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
                baseList = it
                pokemonList = it
                notifyDataSetChanged()
            }
            cl_grp_pokemon_details.visibility = View.VISIBLE
            pgr_loading.visibility = View.GONE
        })
        nationalDexViewModel.pokemon.observe(viewLifecycleOwner, Observer {
            findNavController().navigate(
                NationalDexFragmentDirections
                    .actionNationalDexFragmentToPokemonDetailsFragment(it.pokemonName)
            )
        })
        setUpRecycler()
        setUpFilter()
        setUpFloatingMenu()

        nationalDexViewModel.getPokemonList()
    }

    private fun setUpFloatingMenu() {
        fab.setOnClickListener {
            if (isMenuOpen) {
                ViewUtils.rotateFabBackward(it)
                hideFloatingMenu()
            } else {
                ViewUtils.rotateFabForward(it)
                showFloatingMenu()
            }
        }
        fab_scroll_bottom.setOnClickListener {
            rv_national_dex.smoothScrollToPosition(nationalDexAdapter.pokemonList.lastIndex)
        }
        fab_scroll_top.setOnClickListener {
            rv_national_dex.smoothScrollToPosition(0)
        }
    }

    private fun showFloatingMenu() {
        isMenuOpen = true
        fab_scroll_bottom.animate().translationY(-resources.getDimension(R.dimen.fab_bottom_spacing))
        fab_scroll_top.animate().translationY(-resources.getDimension(R.dimen.fab_top_spacing))
    }

    private fun hideFloatingMenu() {
        isMenuOpen = false
        fab_scroll_bottom.animate().translationY(0f)
        fab_scroll_top.animate().translationY(0f)
    }

    private fun setUpFilter() {
        edt_filter_dex.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Do Nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 0) {
                    nationalDexAdapter.apply {
                        pokemonList = baseList
                        notifyDataSetChanged()
                    }
                } else {
                    nationalDexAdapter.apply {
                        pokemonList = baseList.filter {
                            val input = s.toString()
                            if (input.matches(Regex("[0-9]+")) && it.dexNo != null) {
                                it.dexNo!!.contains(s.toString())
                            } else {
                                it.name.contains(s.toString())
                            }
                        }
                        notifyDataSetChanged()
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //Do Nothing
            }

        })
    }

    private fun setUpRecycler() {
        rv_national_dex.apply {
            layoutManager = LinearLayoutManager(requireContext())
            nationalDexAdapter =
                NationalDexAdapter(requireContext(), mutableListOf(), object : NationalDexListener {
                    override fun onPokemonClicked(pokemon: PokemonListSingle) {
                        cl_grp_pokemon_details.visibility = View.GONE
                        pgr_loading.visibility = View.VISIBLE
                        nationalDexViewModel.getSinglePokemon(pokemon.name.toLowerCase(Locale.ENGLISH))
                    }
                })
            adapter = nationalDexAdapter
        }
    }
}