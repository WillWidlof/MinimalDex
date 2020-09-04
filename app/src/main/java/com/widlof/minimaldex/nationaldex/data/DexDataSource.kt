package com.widlof.minimaldex.nationaldex.data

import android.graphics.Bitmap
import com.widlof.minimaldex.nationaldex.data.model.EvolutionChainResponse
import com.widlof.minimaldex.nationaldex.data.model.NationalDexResponse
import com.widlof.minimaldex.nationaldex.data.model.SpeciesResponse
import com.widlof.minimaldex.pokemondetails.data.model.PokemonResponse

interface DexDataSource {
    suspend fun getNationalDex(): NationalDexResponse?
    suspend fun getSinglePokemonMainJson(name: String): PokemonResponse?
    suspend fun getSprite(spriteUrl: String): Bitmap?
    suspend fun getSpeciesBase(url: String): SpeciesResponse?
    suspend fun getEvolutionChain(url: String): EvolutionChainResponse?
}