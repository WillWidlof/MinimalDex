package com.widlof.minimaldex.nationaldex.data

import android.graphics.Bitmap
import com.widlof.minimaldex.nationaldex.data.model.NationalDexResponse
import com.widlof.minimaldex.pokemondetails.data.model.PokemonResponse
import com.widlof.minimaldex.network.NetworkResponse

class DexRepository(private val dataSource: DexDataSource): DexDataSource {
    private var nationalDexResponse: NationalDexResponse? = null

    override suspend fun getNationalDex(): NetworkResponse<NationalDexResponse?> {
        nationalDexResponse?.let {
            return NetworkResponse.Success(it)
        }
        val response = dataSource.getNationalDex()
        if (response is NetworkResponse.Success && response.responseBody != null) {
            nationalDexResponse = response.responseBody
        }
        return response
    }

    override suspend fun getSinglePokemonMainJson(name: String): NetworkResponse<PokemonResponse?> {
        return dataSource.getSinglePokemonMainJson(name)
    }

    override suspend fun getSprite(spriteUrl: String): NetworkResponse<Bitmap?> {
        return dataSource.getSprite(spriteUrl)
    }
}