package com.widlof.minimaldex.pokemondetails.data.model

data class PokemonResponse(val name: String,
                           val sprites: PokemonSpritesResponse,
                           val moves: List<PokemonMoveResponse>,
                           val stats: List<PokemonStatResponse>,
                           val types: List<PokemonTypeResponse>,
                           val species: PokemonSpeciesResponse
)

data class PokemonSpritesResponse(val front_default: String?,
                                  val back_default: String?,
                                  val front_female: String?,
                                  val back_female: String?,
                                  val front_shiny: String?,
                                  val back_shiny: String?,
                                  val front_shiny_female: String?,
                                  val back_shiny_female: String?)

data class PokemonMoveResponse(val move: PokemonMove)

data class PokemonStatResponse(val stat: PokemonStat)

data class PokemonTypeResponse(val type: PokemonType)

data class PokemonSpeciesResponse(val name: String, val url: String)