package com.widlof.minimaldex.pokemondetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.widlof.minimaldex.R
import com.widlof.minimaldex.nationaldex.DexViewModelFactory
import com.widlof.minimaldex.nationaldex.data.model.PokedexNumbersResponse
import com.widlof.minimaldex.nationaldex.data.model.Species
import com.widlof.minimaldex.pokemondetails.data.model.PokemonExtraDetails
import com.widlof.minimaldex.pokemondetails.data.model.PokemonMove
import com.widlof.minimaldex.pokemondetails.data.model.PokemonSingle
import com.widlof.minimaldex.pokemondetails.data.model.PokemonStat
import com.widlof.minimaldex.pokemondetails.type.TypeBackground
import com.widlof.minimaldex.util.ColourUtils
import com.widlof.minimaldex.util.StringUtils.Companion.capitaliseAll
import kotlinx.android.synthetic.main.dex_numbers.view.*
import kotlinx.android.synthetic.main.fragment_pokemon_details.*
import kotlinx.android.synthetic.main.pokemon_evolution.view.*
import kotlinx.android.synthetic.main.pokemon_extras.view.*
import kotlinx.android.synthetic.main.pokemon_move.view.*
import kotlinx.android.synthetic.main.pokemon_sprite.view.*
import kotlinx.android.synthetic.main.pokemon_stat.view.*
import kotlinx.android.synthetic.main.pokemon_type.view.*


class PokemonDetailsFragment : Fragment() {

    private lateinit var viewModel: PokemonDetailsViewModel
    private lateinit var typeBackground: TypeBackground
    private var isMovesExpanded = false
    private var loadingBar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pokemon_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, DexViewModelFactory()
        ).get(PokemonDetailsViewModel::class.java)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    findNavController().apply {
                        navigateUp()
                        popBackStack(R.id.action_nationalDexFragment_to_pokemonDetailsFragment, true)
                    }
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        setUpObservers()
    }

    private fun setUpObservers() {
        val key = arguments?.get(POKEMON_KEY) as String
        observePokemonDetails()
        observeLoadingState()
        viewModel.findPokemon(key)
    }

    private fun observeLoadingState() {
        viewModel.isLoadingEvolution.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                loadingBar = Snackbar
                    .make(cl_grp_pokemon_details, viewModel.getLoadingReason(), Snackbar.LENGTH_INDEFINITE)
                    .setTextColor(ColourUtils.getColour(requireContext(), R.color.textColour))
                    .setDuration(8000).also {
                        it.show()
                    }
            } else {
                loadingBar?.let {
                    it.dismiss()
                }
            }
        })
    }

    private fun observePokemonDetails() {
        viewModel.pokemon.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                isMovesExpanded = false
                tv_pokemon_name.text = it.pokemonName.capitaliseAll()
                inc_sprites.iv_front_sprite.setImageBitmap(it.normalMaleFrontSprite)
                if (it.normalMaleBackSprite != null) {
                    inc_sprites.iv_back_sprite.setImageBitmap(it.normalMaleBackSprite)
                } else {
                    inc_sprites.iv_back_sprite.visibility = View.GONE
                }
                setTypes(it)
                setStats(it.statList)
                setMoves(it.moveList)
                setEvolutions(it.species)
                setFlavourText(it.species?.flavourText)
                setExtraDetails(it.species?.extraDetails)
                setPokedexNumbers(it.species?.dexNumbers)
                setLoadComplete()
            } else {
                //missing Error case
            }
        })
    }

    private fun setLoadComplete() {
        cl_grp_pokemon_details.visibility = View.VISIBLE
        pgr_loading.visibility = View.GONE
        viewModel.loadingEvolutionComplete()
    }

    private fun setPokedexNumbers(dexNumbers: List<PokedexNumbersResponse>?) {
        ll_pokedex_numbers.removeAllViews()
        dexNumbers?.let {
            for (dex in it) {
                val dexNumView = LayoutInflater.from(requireContext())
                    .inflate(R.layout.dex_numbers, null).apply {
                        tv_dex_name.text = dex.pokedex?.name?.capitaliseAll()
                        tv_dex_no.text = dex.entry_number?.toString()
                    }
                ll_pokedex_numbers.addView(dexNumView)
            }
        }
    }

    private fun setExtraDetails(extraDetails: PokemonExtraDetails?) {
        ll_pokemon_extras.removeAllViews()
        extraDetails?.let {
            val extrasView = LayoutInflater.from(requireContext())
                .inflate(R.layout.pokemon_extras, null).apply {
                    tv_capture_rate_value.text = it.captureRate?.toString()
                    tv_base_happiness_value.text = it.baseHappiness?.toString()
                }
            ll_pokemon_extras.addView(extrasView)
        }
    }

    private fun setFlavourText(flavourText: String?) {
        if (flavourText != null) {
            tv_pokemon_flavour_text.text = flavourText.replace("\n", " ", true)
        } else {
            tv_pokemon_flavour_text.visibility = View.GONE
        }
    }

    private fun setEvolutions(species: Species?) {
        ll_pokemon_evolution.removeAllViews()
        species?.let {
            it.evolutions?.let { list ->
                for (evolution in list) {
                    val view = LayoutInflater.from(requireContext())
                        .inflate(R.layout.pokemon_evolution, null).apply {
                            evolution.evolutionName?.let { pokemonName ->
                                tv_pokemon_evolution_name.text = pokemonName.capitalize()
                                cl_evolution.setOnClickListener {
                                    if (!viewModel.isLoadingEvolution.value!!) {
                                        viewModel.getPokemonEvolvedForm(pokemonName)
                                    }
                                }
                            }
                        }
                    ll_pokemon_evolution.addView(view)
                }
            }
        }
    }

    private fun setMoves(moveList: List<PokemonMove>?) {
        ll_pokemon_moves.removeAllViews()
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
        ll_stats.removeAllViews()
        val statBackground = StatBarBackground(requireContext())
        statList?.let {
            for (stat in statList) {
                val view =
                    LayoutInflater.from(requireContext()).inflate(R.layout.pokemon_stat, null)
                view.apply {
                    tv_stat_name.text = stat.name.capitaliseAll()
                    tv_stat_value.text = stat.value
                    val progressColour = statBackground.provideBarColour(stat.name)
                    pgr_stat_value.progressDrawable.setTint(
                        ContextCompat.getColor(
                            requireContext(),
                            progressColour
                        )
                    )
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
                inc_pokemon_type.tv_pokemon_type_one.apply {
                    text = it[0].name.capitalize()
                    background = typeBackground.provideBackground(it[0].name)
                }
            }
            inc_pokemon_type.tv_pokemon_type_two.apply {
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