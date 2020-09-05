package com.widlof.minimaldex.nationaldex.data.model

data class SpeciesResponse(val evolution_chain: EvolutionChainBase,
                           val flavor_text_entries: List<SpeciesFlavourTextResponse>?,
                           val base_happiness: Int, val capture_rate: Int,
                           val pokedex_numbers: List<PokedexNumbersResponse>)

data class EvolutionChainBase(val url: String)

data class SpeciesFlavourTextResponse(val flavor_text: String,
                                      val language: SpeciesFlavourTextLanguage,
                                      val version: SpeciesFlavourVersionResponse)

data class SpeciesFlavourTextLanguage(val name: String)

data class SpeciesFlavourVersionResponse(val name: String,
                                         val url: String)

data class EvolutionChainResponse(val chain: EvolutionChainEvolvesToBase)

data class EvolutionChainEvolvesToBase(val evolves_to: List<EvolutionChainEvolvesToChild>?,
                                       val species: SpeciesChildResponse?)

data class EvolutionChainEvolvesToChild(val evolves_to: List<EvolutionChainSpeciesChild>?,
                                        val species: SpeciesChildResponse?)

data class EvolutionChainSpeciesChild(val species: SpeciesChildResponse?)

data class SpeciesChildResponse(val name: String,
                                val url: String)

data class PokedexNumbersResponse(val entry_number: Int?, val pokedex: PokedexTypeResponse?)

data class PokedexTypeResponse(val name: String)