package com.widlof.minimaldex.nationaldex.data

import android.graphics.Bitmap
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

                with(response.responseBody) {
                    val pokemon =  PokemonSingle(
                        name,
                        frontSprite,
                        backSprite,
                        sprites,
                        moves,
                        stats,
                        types,
                        null
                    )
                    PokemonCache.pokemonCache[name] = pokemon
                    return PokemonCache.pokemonCache[name]
                }
            }
            return null
        }
        return null

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