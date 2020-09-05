package com.widlof.minimaldex.nationaldex.data.interactor

import com.widlof.minimaldex.nationaldex.data.model.EvolutionChainResponse
import com.widlof.minimaldex.pokemondetails.data.model.PokemonEvolution

class GetEvolutionListInteractor {
    fun buildEvolutionList(responseBody: EvolutionChainResponse): MutableList<PokemonEvolution> {
        val list: MutableList<PokemonEvolution> = mutableListOf<PokemonEvolution>()
        with(responseBody.chain) {
            list.add(PokemonEvolution(species?.name, species?.url))
            if(evolves_to.isNotEmpty()) {
                val evo = evolves_to[0].species
                evo?.let {
                    list.add(PokemonEvolution(it.name, it.url))
                }
                val finalEvo = evolves_to[0].evolves_to
                if (finalEvo.isNotEmpty()) {
                    val evoThree = finalEvo[0].species
                    evoThree?.let {
                        list.add(PokemonEvolution(evoThree.name, evoThree.url))
                    }
                }
            }
        }
        return list
    }
}