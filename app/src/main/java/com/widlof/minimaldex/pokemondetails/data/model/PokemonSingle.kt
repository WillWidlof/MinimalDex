package com.widlof.minimaldex.pokemondetails.data.model

import android.graphics.Bitmap

data class PokemonSingle(val pokemonName: String,
                         val normalMaleFrontSprite: Bitmap?,
                         val normalMaleBackSprite: Bitmap?,
                         val pokemonSpritesResponse: PokemonSpritesResponse?,
                         val moveList: List<PokemonMove>?,
                         val statList: List<PokemonStat>?,
                         val typeList: List<PokemonType>?,
                         val evolutionList: List<PokemonEvolution>?)

data class PokemonMove(val name: String, val url: String)

data class PokemonStat(val name: String, val url: String)

data class PokemonType(val name: String, val url: String)

data class PokemonEvolution(val evolutionName: String, val evolutionUrl: String)