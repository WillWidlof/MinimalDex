package com.widlof.minimaldex.nationaldex.data

import android.graphics.Bitmap
import com.widlof.minimaldex.nationaldex.data.model.NationalDexResponse
import com.widlof.minimaldex.pokemondetails.data.model.PokemonResponse
import com.widlof.minimaldex.network.NetworkResponse

interface DexDataSource {
    suspend fun getNationalDex(): NetworkResponse<NationalDexResponse?>
    suspend fun getSinglePokemonMainJson(name: String): NetworkResponse<PokemonResponse?>
    suspend fun getSprite(spriteUrl: String): NetworkResponse<Bitmap?>
}