package com.widlof.minimaldex.nationaldex.data.model

import com.widlof.minimaldex.pokemondetails.data.model.PokemonEvolution
import com.widlof.minimaldex.pokemondetails.data.model.PokemonExtraDetails

data class Species(
    val flavourText: String,
    val evolutions: List<PokemonEvolution>?,
    val extraDetails: PokemonExtraDetails?,
    val dexNumbers: List<PokedexNumbersResponse>?
)