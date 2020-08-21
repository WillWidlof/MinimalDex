package com.widlof.minimaldex.pokemondetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.widlof.minimaldex.R
import com.widlof.minimaldex.pokemondetails.data.PokemonCache
import kotlinx.android.synthetic.main.fragment_pokemon_details.*


class PokemonDetailsFragment : Fragment() {

    private lateinit var viewModel: PokemonDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pokemon_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PokemonDetailsViewModel::class.java)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    findNavController().apply {
                        navigateUp()
                        popBackStack()
                    }
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val key = arguments?.get(POKEMON_KEY) as String
        val pokemon = PokemonCache.pokemonCache[key]
        tv_pokemon_name.text = pokemon?.pokemonName
        iv_front_sprite.setImageBitmap(pokemon?.normalMaleFrontSprite)
    }

    companion object {
        const val POKEMON_KEY = "pokemonName"
    }

}