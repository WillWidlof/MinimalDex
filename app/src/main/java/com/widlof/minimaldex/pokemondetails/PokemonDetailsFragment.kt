package com.widlof.minimaldex.pokemondetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.widlof.minimaldex.R
import com.widlof.minimaldex.nationaldex.data.model.Species
import com.widlof.minimaldex.pokemondetails.data.PokemonCache
import com.widlof.minimaldex.pokemondetails.data.model.PokemonMove
import com.widlof.minimaldex.pokemondetails.data.model.PokemonSingle
import com.widlof.minimaldex.pokemondetails.data.model.PokemonStat
import com.widlof.minimaldex.pokemondetails.type.TypeBackground
import kotlinx.android.synthetic.main.fragment_pokemon_details.*
import kotlinx.android.synthetic.main.pokemon_evolution.view.*
import kotlinx.android.synthetic.main.pokemon_move.view.*
import kotlinx.android.synthetic.main.pokemon_stat.view.*


class PokemonDetailsFragment : Fragment() {

    private lateinit var viewModel: PokemonDetailsViewModel
    private lateinit var typeBackground: TypeBackground
    private var isMovesExpanded = false

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
        tv_pokemon_name.text = pokemon?.pokemonName?.capitalize()
        iv_front_sprite.setImageBitmap(pokemon?.normalMaleFrontSprite)
        iv_back_sprite.setImageBitmap(pokemon?.normalMaleBackSprite)
        setTypes(pokemon)
        setStats(pokemon?.statList)
        setMoves(pokemon?.moveList)
        setEvolutions(pokemon?.species)
        setFlavourText(pokemon?.species?.flavourText)
    }

    private fun setFlavourText(flavourText: String?) {
        if (flavourText != null) {
            tv_pokemon_flavour_text.text = flavourText.replace("\n", " ", true)
        } else {
            tv_pokemon_flavour_text.visibility = View.GONE
        }
    }

    private fun setEvolutions(species: Species?) {
        species?.let {
            it.evolutions?.let { list ->
                for (evolution in list) {
                    val view = LayoutInflater.from(requireContext())
                        .inflate(R.layout.pokemon_evolution, null).apply {
                            tv_pokemon_evolution_name.text = evolution.evolutionName?.capitalize()
                            cl_evolution.setOnClickListener {
                                //Handle showing evolved form
                            }
                        }
                    ll_pokemon_evolution.addView(view)
                }
            }
        }
    }

    private fun setMoves(moveList: List<PokemonMove>?) {
        moveList?.let {
            val listOfList = it.chunked(2)
            for (list in listOfList) {
                val view =
                    LayoutInflater.from(requireContext()).inflate(R.layout.pokemon_move, null)
                if (list.isNotEmpty()) {
                    setMoveItem(view.tv_pokemon_move_one, list[0])
                    if (list.size > 1) {
                        setMoveItem(view.tv_pokemon_move_two, list[1])
                    }
                }
                ll_pokemon_moves.addView(view)
            }
        }
        setMoveTitleExpand()
    }

    private fun setMoveTitleExpand() {
        tv_pokemon_move_title.setOnClickListener {
            isMovesExpanded = if (isMovesExpanded) {
                ll_pokemon_moves.visibility = View.GONE
                tv_move_info.visibility = View.VISIBLE
                false
            } else {
                ll_pokemon_moves.visibility = View.VISIBLE
                tv_move_info.visibility = View.GONE
                true
            }
        }
    }

    private fun setMoveItem(view: TextView, pokemonMove: PokemonMove) {
        view.apply {
            text = pokemonMove.name.capitalize()
            setOnClickListener {
                //launch move details with move URL.
            }
        }
    }

    private fun setStats(statList: List<PokemonStat>?) {
        val statBackground = StatBarBackground(requireContext())
        statList?.let {
            for (stat in statList) {
                val view =
                    LayoutInflater.from(requireContext()).inflate(R.layout.pokemon_stat, null)
                view.apply {
                    tv_stat_name.text = stat.name.capitalize()
                    tv_stat_value.text = stat.value
                    val progressColour = statBackground.provideBarColour(stat.name)
                    pgr_stat_value.progressDrawable.setTint(
                        ContextCompat.getColor(requireContext(),
                        progressColour))
                    stat.value?.toInt()?.let {
                        pgr_stat_value.progress = it
                    }
                }
                ll_stats.addView(view)
            }
        }
    }

    private fun setTypes(pokemon: PokemonSingle?) {
        typeBackground = TypeBackground(requireContext())
        pokemon?.typeList?.let {
            if (it.isNotEmpty()) {
                tv_pokemon_type_one.apply {
                    text = it[0].name.capitalize()
                    background = typeBackground.provideBackground(it[0].name)
                }
            }
            tv_pokemon_type_two.apply {
                if (it.size > 1) {
                    text = it[1].name.capitalize()
                    background = typeBackground.provideBackground(it[1].name)
                } else {
                    visibility = View.GONE
                }
            }
        }
    }

    companion object {
        const val POKEMON_KEY = "pokemonName"
    }

}