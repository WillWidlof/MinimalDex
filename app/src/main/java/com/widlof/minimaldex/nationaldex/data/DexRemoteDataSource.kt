package com.widlof.minimaldex.nationaldex.data

import android.graphics.Bitmap
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.widlof.minimaldex.nationaldex.data.model.EvolutionChainResponse
import com.widlof.minimaldex.nationaldex.data.model.NationalDexResponse
import com.widlof.minimaldex.nationaldex.data.model.SpeciesResponse
import com.widlof.minimaldex.network.NetworkRequestSender
import com.widlof.minimaldex.pokemondetails.data.model.PokemonResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class DexRemoteDataSource(dispatcher: CoroutineDispatcher,
    private val networkRequestSender: NetworkRequestSender) : DexDataSource {

    private var scope: CoroutineScope = CoroutineScope(dispatcher)

    override suspend fun getNationalDex(): NationalDexResponse? =
        suspendCoroutine { continuation ->
            scope.launch {
                runCatching {
                    networkRequestSender.makeJsonRequest(NATION_DEX_URL)
                }.onSuccess {
                    if (it != null) {
                        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                        val adapter: JsonAdapter<NationalDexResponse> =
                            moshi.adapter(NationalDexResponse::class.java)
                        val response = adapter.fromJson(it)
                        response?.getPokemonList()?.forEachIndexed { index, pokemonSingle ->
                            pokemonSingle.dexNo = (index + 1).toString()
                        }
                        continuation.resumeWith(Result.success(response))
                    } else {
                        continuation.resumeWith(Result.failure(NullPointerException(NATION_DEX_EXCEPTION)))
                    }
                }.onFailure {
                    continuation.resumeWithException(it)
                }
            }
        }

    override suspend fun getSinglePokemonMainJson(name: String): PokemonResponse? =
        suspendCoroutine { continuation ->
            scope.launch {
                val url = POKEMON_URL + name.toLowerCase()
                runCatching {
                    networkRequestSender.makeJsonRequest(url)
                }.onSuccess {
                    if (it != null) {
                        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                        val adapter: JsonAdapter<PokemonResponse> =
                            moshi.adapter(PokemonResponse::class.java)
                        val result = adapter.fromJson(it)
                        continuation.resumeWith(Result.success(result))
                    } else {
                        continuation.resumeWithException(NullPointerException(SINGLE_POKEMON_EXCEPTION))
                    }
                }.onFailure {
                    continuation.resumeWithException(it)
                }
            }
        }

    override suspend fun getSprite(spriteUrl: String): Bitmap? =
        suspendCoroutine { continuation ->
            scope.launch {
                runCatching {
                    networkRequestSender.makeImageRequest(spriteUrl)
                }.onSuccess {
                    if (it != null) {
                        continuation.resumeWith(Result.success(it))
                    } else {
                        continuation.resumeWithException(NullPointerException(SPRITE_EXCEPTION))
                    }
                }.onFailure {
                    continuation.resumeWithException(it)
                }
            }
        }

    override suspend fun getSpeciesBase(url: String): SpeciesResponse? =
        suspendCoroutine { continuation ->
            scope.launch {
                runCatching {
                    networkRequestSender.makeJsonRequest(url)
                }.onSuccess {
                    if (it != null) {
                        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                        val adapter: JsonAdapter<SpeciesResponse> =
                            moshi.adapter(SpeciesResponse::class.java)
                        val result = adapter.fromJson(it)
                        continuation.resumeWith(Result.success(result))
                    } else {
                        continuation.resumeWith(Result.failure(NullPointerException(SPECIES_EXCEPTION)))
                    }
                }.onFailure {
                    continuation.resumeWithException(it)
                }
            }
        }

    override suspend fun getEvolutionChain(url: String): EvolutionChainResponse? =
        suspendCoroutine { continuation ->
            scope.launch {
                runCatching {
                    networkRequestSender.makeJsonRequest(url)
                }.onSuccess {
                    if (it != null) {
                        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                        val adapter: JsonAdapter<EvolutionChainResponse> =
                            moshi.adapter(EvolutionChainResponse::class.java)
                        val result = adapter.fromJson(it)
                        continuation.resumeWith(Result.success(result))
                    } else {
                        continuation.resumeWith(
                            Result.failure(NullPointerException(EVOLUTION_CHAIN_EXCEPTION)))
                    }
                }.onFailure {
                    continuation.resumeWithException(it)
                }
            }
        }

    companion object {
        private const val POKEMON_URL = "https://pokeapi.co/api/v2/pokemon/"
        private const val NATION_DEX_URL = "https://pokeapi.co/api/v2/pokemon?limit=2000"
        private const val NATION_DEX_EXCEPTION = "National Dex JSON is null"
        private const val SINGLE_POKEMON_EXCEPTION = "Single Pokemon JSON is null"
        private const val SPRITE_EXCEPTION = "Sprite is null"
        private const val SPECIES_EXCEPTION = "Species Base is null"
        private const val EVOLUTION_CHAIN_EXCEPTION = "Evolution Chain JSON is null"
    }
}