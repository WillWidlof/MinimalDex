package com.widlof.minimaldex.nationaldex.data.interactor

import com.widlof.minimaldex.nationaldex.data.DexDataSource
import com.widlof.minimaldex.nationaldex.data.model.Species
import com.widlof.minimaldex.nationaldex.data.model.SpeciesResponse
import com.widlof.minimaldex.pokemondetails.data.model.PokemonEvolution
import com.widlof.minimaldex.pokemondetails.data.model.PokemonExtraDetails
import com.widlof.minimaldex.pokemondetails.data.model.PokemonSpeciesResponse

class GetSpeciesInteractor(private val repository: DexDataSource,
                           private val getEvolutionListInteractor: GetEvolutionListInteractor) {
    suspend fun getSpecies(speciesResponse: PokemonSpeciesResponse): Species? {
        val speciesResponse = repository.getSpeciesBase(speciesResponse.url)
        if (speciesResponse != null) {
            val extraDetails = getExtraDetails(speciesResponse)
            val dexNumbers = speciesResponse.pokedex_numbers
            var evoList: MutableList<PokemonEvolution> = mutableListOf<PokemonEvolution>()
            with(speciesResponse) {
                val evolutionChain = repository.getEvolutionChain(this.evolution_chain.url)
                if (evolutionChain != null) {
                    evoList = getEvolutionListInteractor.buildEvolutionList(evolutionChain)
                }
                var flavourText = ""
                if (flavor_text_entries != null && flavor_text_entries.isNotEmpty()) {
                     flavourText = flavor_text_entries.filter {
                        it.language.name == ENGLISH
                    }[0].flavor_text
                }
                return Species(flavourText, evoList, extraDetails, dexNumbers)
            }
        }
        return null
    }

    private fun getExtraDetails(responseBody: SpeciesResponse): PokemonExtraDetails {
        return PokemonExtraDetails(responseBody.capture_rate, responseBody.base_happiness)
    }

    companion object {
        private const val ENGLISH = "en"
    }
}