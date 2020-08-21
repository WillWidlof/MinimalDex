package com.widlof.minimaldex.nationaldex.data

import android.graphics.Bitmap
import com.widlof.minimaldex.pokemondetails.data.model.PokemonSingle
import com.widlof.minimaldex.network.NetworkResponse
import com.widlof.minimaldex.pokemondetails.data.PokemonCache

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

                with(response.responseBody) {
                    val pokemon =  PokemonSingle(
                        name,
                        frontSprite,
                        backSprite,
                        sprites,
                        null,
                        null,
                        null,
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