package com.widlof.minimaldex.pokemondetails.data.model

import android.graphics.Bitmap
import com.widlof.minimaldex.nationaldex.data.model.Species

data class PokemonSingle(
    val pokemonName: String,
    val normalMaleFrontSprite: Bitmap?,
    val normalMaleBackSprite: Bitmap?,
    val pokemonSpritesResponse: PokemonSpritesResponse?,
    val moveList: List<PokemonMove>?,
    val statList: List<PokemonStat>?,
    val typeList: List<PokemonType>?,
    val species: Species?
)

data class PokemonMove(val name: String, val url: String)

data class PokemonStat(val name: String, val url: String, var value: String?)

data class PokemonType(val name: String, val url: String)

data class PokemonEvolution(val evolutionName: String?, val evolutionUrl: String?)

data class PokemonExtraDetails(val captureRate: Int, val baseHappiness: Int)