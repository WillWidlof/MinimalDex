package com.widlof.minimaldex.nationaldex.data

import android.graphics.Bitmap
import com.widlof.minimaldex.nationaldex.data.model.EvolutionChainResponse
import com.widlof.minimaldex.nationaldex.data.model.Species
import com.widlof.minimaldex.nationaldex.data.model.SpeciesResponse
import com.widlof.minimaldex.network.NetworkResponse
import com.widlof.minimaldex.pokemondetails.data.PokemonCache
import com.widlof.minimaldex.pokemondetails.data.model.*

class GetSinglePokemonInteractor(private val repository: DexDataSource) {
    suspend fun getSinglePokemon(name: String): PokemonSingle? {
        PokemonCache.pokemonCache[name]?.let {
            return it
        }
        val response = repository.getSinglePokemonMainJson(name)
        if (response is NetworkResponse.Success) {
            response.responseBody?.let {
                val frontSprite = getSprite(it.sprites.front_default)
                val backSprite = getSprite(it.sprites.back_default)
                val types = getTypes(it.types)
                val moves = getMoves(it.moves)
                val stats = getStats(it.stats)
                val species = getSpecies(it.species)

                with(response.responseBody) {
                    val pokemon = PokemonSingle(
                        name,
                        frontSprite,
                        backSprite,
                        sprites,
                        moves,
                        stats,
                        types,
                        species
                    )
                    PokemonCache.pokemonCache[name] = pokemon
                    return PokemonCache.pokemonCache[name]
                }
            }
            return null
        }
        return null

    }

    private suspend fun getSpecies(speciesResponse: PokemonSpeciesResponse): Species? {
        val speciesResponse = repository.getSpeciesBase(speciesResponse.url)
        if (speciesResponse is NetworkResponse.Success && speciesResponse.responseBody != null) {
            val extraDetails = getExtraDetails(speciesResponse.responseBody)
            val dexNumbers = speciesResponse.responseBody.pokedex_numbers
            var evoList: MutableList<PokemonEvolution> = mutableListOf<PokemonEvolution>()
            with(speciesResponse.responseBody) {
                val evolutionChain = repository.getEvolutionChain(this.evolution_chain.url)
                if (evolutionChain is NetworkResponse.Success && evolutionChain.responseBody != null) {
                    evoList = buildEvolutionList(evolutionChain.responseBody)
                }
                return Species(this.flavor_text_entries.first().flavor_text, evoList, extraDetails, dexNumbers)
            }
        }
        return null
    }

    private fun getExtraDetails(responseBody: SpeciesResponse): PokemonExtraDetails {
        return PokemonExtraDetails(responseBody.capture_rate, responseBody.base_happiness)
    }

    private fun buildEvolutionList(responseBody: EvolutionChainResponse): MutableList<PokemonEvolution> {
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

    private fun getStats(stats: List<PokemonStatResponse>): List<PokemonStat> {
        val statList = mutableListOf<PokemonStat>()
        for (stat in stats) {
            statList.add(stat.stat.apply {
                value = stat.base_stat
            })
        }
        return statList
    }

    private fun getMoves(moves: List<PokemonMoveResponse>): List<PokemonMove> {
        val moveList = mutableListOf<PokemonMove>()
        for (move in moves) {
            moveList.add(move.move)
        }
        return moveList
    }

    private fun getTypes(types: List<PokemonTypeResponse>): List<PokemonType>? {
        val typeList = mutableListOf<PokemonType>()
        for (type in types) {
            with(type.type) {
                typeList.add(PokemonType(name = name, url = url))
            }
        }
        return typeList
    }

    private suspend fun getSprite(url: String?): Bitmap? {
        return if (url != null) {
            val response = repository.getSprite(url)
            if (response is NetworkResponse.Success) {
                response.responseBody
            } else {
                null
            }
        } else {
            null
        }
    }
}