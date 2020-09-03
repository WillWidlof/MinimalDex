package com.widlof.minimaldex.nationaldex.data

import com.widlof.minimaldex.network.NetworkRequestSender
import com.widlof.minimaldex.network.NetworkResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import java.lang.NullPointerException

@ExperimentalCoroutinesApi
class DexRemoteDataSourceTest {

    private lateinit var dexRemoteDataSource: DexRemoteDataSource

    @MockK
    private lateinit var networkRequestSender: NetworkRequestSender

    @MockK
    private lateinit var jsonResponse: NetworkResponse.Success<String>

    @MockK
    private lateinit var errorResponse: NetworkResponse.Error

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        dexRemoteDataSource = DexRemoteDataSource(Dispatchers.Unconfined, networkRequestSender)
    }

    @Test
    fun `test getNationalDex returns a success network response`() = runBlockingTest {
        coEvery { networkRequestSender.makeJsonRequest(NATIONAL_DEX_URL) }.returns(jsonResponse)
        every { jsonResponse.responseBody }.returns(NATIONAL_DEX_JSON)
        val response = dexRemoteDataSource.getNationalDex()
        response as NetworkResponse.Success
        response.responseBody?.let {
            assertEquals(3, it.getPokemonList()?.size)
            assertEquals(SQUIRTLE, it.getPokemonList()?.get(0)?.name)
            assertEquals(SQUIRTLE_URL, it.getPokemonList()?.get(0)?.url)
            assertEquals(WARTORTLE, it.getPokemonList()?.get(1)?.name)
            assertEquals(WARTORTLE_URL, it.getPokemonList()?.get(1)?.url)
            assertEquals(BLASTOISE, it.getPokemonList()?.get(2)?.name)
            assertEquals(BLASTOISE_URL, it.getPokemonList()?.get(2)?.url)
        }
    }

    @Test
    fun `test getNationalDex returns an error network response`() = runBlockingTest {
        coEvery { networkRequestSender.makeJsonRequest(NATIONAL_DEX_URL) }.returns(errorResponse)
        every { errorResponse.errorCode }.returns(ERROR_CODE)
        val response = dexRemoteDataSource.getNationalDex()
        response as NetworkResponse.Error
        response.errorCode.let {
            assertEquals(ERROR_CODE, it)
        }
    }

    @Test
    fun getSinglePokemonMainJson() {
    }

    @Test
    fun getSprite() {
    }

    @Test
    fun getSpeciesBase() {
    }

    @Test
    fun getEvolutionChain() {
    }

    companion object {
        private const val NATIONAL_DEX_JSON = "{\"count\": 1050, \"next\": \"https://pokeapi.co/api/v2/pokemon?offset=20&limit=20\", \"previous\": null, \"results\": [{\"name\": \"squirtle\", \"url\": \"https://pokeapi.co/api/v2/pokemon/7/\"}, {\"name\": \"wartortle\", \"url\": \"https://pokeapi.co/api/v2/pokemon/8/\"}, {\"name\": \"blastoise\", \"url\": \"https://pokeapi.co/api/v2/pokemon/9/\"}]}"
        private const val SQUIRTLE = "squirtle"
        private const val WARTORTLE = "wartortle"
        private const val BLASTOISE = "blastoise"
        private const val SQUIRTLE_URL = "https://pokeapi.co/api/v2/pokemon/7/"
        private const val WARTORTLE_URL = "https://pokeapi.co/api/v2/pokemon/8/"
        private const val BLASTOISE_URL = "https://pokeapi.co/api/v2/pokemon/9/"
        private const val ERROR_CODE = "404"
        private const val POKEMON_URL = "https://pokeapi.co/api/v2/pokemon/"
        private const val NATIONAL_DEX_URL = "https://pokeapi.co/api/v2/pokemon?limit=2000"
        private const val PARSE_ERROR = "PARSE_ERROR"
    }
}