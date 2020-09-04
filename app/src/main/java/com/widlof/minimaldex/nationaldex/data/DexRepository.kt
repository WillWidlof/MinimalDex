package com.widlof.minimaldex.nationaldex.data

import android.graphics.Bitmap
import com.widlof.minimaldex.nationaldex.data.model.EvolutionChainResponse
import com.widlof.minimaldex.nationaldex.data.model.NationalDexResponse
import com.widlof.minimaldex.nationaldex.data.model.SpeciesResponse
import com.widlof.minimaldex.pokemondetails.data.model.PokemonResponse

class DexRepository(private val dataSource: DexDataSource): DexDataSource {
    private var nationalDexResponse: NationalDexResponse? = null

    fun setCachedNationalDexResponse(nationalDexResponse: NationalDexResponse) {
        this.nationalDexResponse = nationalDexResponse
    }

    override suspend fun getNationalDex(): NationalDexResponse? {
        nationalDexResponse?.let {
            return it
        }
        val response = dataSource.getNationalDex()
        nationalDexResponse = response
        return response
    }

    override suspend fun getSinglePokemonMainJson(name: String): PokemonResponse? {
        return dataSource.getSinglePokemonMainJson(name)
    }

    override suspend fun getSprite(spriteUrl: String): Bitmap? {
        return dataSource.getSprite(spriteUrl)
    }

    override suspend fun getSpeciesBase(url: String): SpeciesResponse? {
        return dataSource.getSpeciesBase(url)
    }

    override suspend fun getEvolutionChain(url: String): EvolutionChainResponse? {
        return dataSource.getEvolutionChain(url)
    }
}