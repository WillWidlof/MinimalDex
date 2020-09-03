package com.widlof.minimaldex.nationaldex.data

import android.graphics.Bitmap
import com.widlof.minimaldex.nationaldex.data.model.EvolutionChainResponse
import com.widlof.minimaldex.nationaldex.data.model.NationalDexResponse
import com.widlof.minimaldex.nationaldex.data.model.SpeciesResponse
import com.widlof.minimaldex.network.NetworkResponse
import com.widlof.minimaldex.pokemondetails.data.model.PokemonResponse
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DexRepositoryTest {

    private lateinit var repository: DexRepository

    @MockK
    private lateinit var dexDataSource: DexDataSource

    @MockK
    private lateinit var bitmapResponse: NetworkResponse.Success<Bitmap>

    @MockK
    private lateinit var nationalDexResponse: NetworkResponse.Success<NationalDexResponse>

    @MockK
    private lateinit var pokemonResponse: NetworkResponse.Success<PokemonResponse>

    @MockK
    private lateinit var speciesResponse: NetworkResponse.Success<SpeciesResponse>

    @MockK
    private lateinit var evolutionChainResponse: NetworkResponse.Success<EvolutionChainResponse>

    @MockK
    private lateinit var errorResponse: NetworkResponse.Error

    @MockK
    private lateinit var bitmap: Bitmap

    @MockK
    private lateinit var nationalDex: NationalDexResponse

    @MockK
    private lateinit var pokemon: PokemonResponse

    @MockK
    private lateinit var species: SpeciesResponse

    @MockK
    private lateinit var evolutionChain: EvolutionChainResponse

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        repository = DexRepository(dexDataSource)
    }

    @Test
    fun `test getNationalDex returns a cached dex network response`() = runBlockingTest {
        repository.setCachedNationalDexResponse(nationalDex)
        val response = repository.getNationalDex()
        response as NetworkResponse.Success
        assertEquals(nationalDex, response.responseBody)
        coVerify { dexDataSource wasNot Called }
    }

    @Test
    fun `test getNationalDex returns a success network response`() = runBlockingTest {
        coEvery { dexDataSource.getNationalDex() }.returns(nationalDexResponse)
        every { nationalDexResponse.responseBody }.returns(nationalDex)
        val response = repository.getNationalDex()
        response as NetworkResponse.Success
        coVerify {
            dexDataSource.getNationalDex()
        }
        assertEquals(nationalDex, response.responseBody)
    }

    @Test
    fun `test getNationalDex returns an error network response`() = runBlockingTest {
        coEvery { dexDataSource.getNationalDex() }.returns(errorResponse)
        every { errorResponse.errorCode }.returns(ERROR_CODE)
        val response = repository.getNationalDex()
        coVerify {
            dexDataSource.getNationalDex()
        }
        response as NetworkResponse.Error
        assertEquals(ERROR_CODE, response.errorCode)
    }

    @Test
    fun `test getSinglePokemonMainJson returns a success network response`() = runBlockingTest {
        coEvery { dexDataSource.getSinglePokemonMainJson(SQUIRTLE) }.returns(pokemonResponse)
        every { pokemonResponse.responseBody }.returns(pokemon)
        val response = repository.getSinglePokemonMainJson(SQUIRTLE)
        coVerify {
            dexDataSource.getSinglePokemonMainJson(SQUIRTLE)
        }
        response as NetworkResponse.Success
        assertEquals(pokemon, response.responseBody)
    }

    @Test
    fun `test getSinglePokemonMainJson returns an error network response`() = runBlockingTest {
        coEvery { dexDataSource.getSinglePokemonMainJson(SQUIRTLE) }.returns(errorResponse)
        every { errorResponse.errorCode }.returns(ERROR_CODE)
        val response = repository.getSinglePokemonMainJson(SQUIRTLE)
        coVerify {
            dexDataSource.getSinglePokemonMainJson(SQUIRTLE)
        }
        response as NetworkResponse.Error
        assertEquals(ERROR_CODE, response.errorCode)
    }

    @Test
    fun `test getSprite returns a success bitmap network response`() = runBlockingTest {
        coEvery { dexDataSource.getSprite(SPRITE_URL) }.returns(bitmapResponse)
        every { bitmapResponse.responseBody }.returns(bitmap)
        val response = repository.getSprite(SPRITE_URL)
        coVerify {
            dexDataSource.getSprite(SPRITE_URL)
        }
        response as NetworkResponse.Success
        assertEquals(bitmap, response.responseBody)
    }

    @Test
    fun `test getSprite returns an error bitmap network response`() = runBlockingTest {
        coEvery { dexDataSource.getSprite(SPRITE_URL) }.returns(errorResponse)
        every { errorResponse.errorCode }.returns(ERROR_CODE)
        val response = repository.getSprite(SPRITE_URL)
        coVerify {
            dexDataSource.getSprite(SPRITE_URL)
        }
        response as NetworkResponse.Error
        assertEquals(ERROR_CODE, response.errorCode)
    }

    @Test
    fun `test getSpeciesBase returns a success speciesBase network response`() = runBlockingTest {
        coEvery { dexDataSource.getSpeciesBase(SPECIES_URL) }.returns(speciesResponse)
        every { speciesResponse.responseBody }.returns(species)
        val response = repository.getSpeciesBase(SPECIES_URL)
        coVerify {
            dexDataSource.getSpeciesBase(SPECIES_URL)
        }
        response as NetworkResponse.Success
        assertEquals(species, response.responseBody)
    }

    @Test
    fun `test getSprite returns an error speciesBase network response`() = runBlockingTest {
        coEvery { dexDataSource.getSpeciesBase(SPECIES_URL) }.returns(errorResponse)
        every { errorResponse.errorCode }.returns(ERROR_CODE)
        val response = repository.getSpeciesBase(SPECIES_URL)
        coVerify {
            dexDataSource.getSpeciesBase(SPECIES_URL)
        }
        response as NetworkResponse.Error
        assertEquals(ERROR_CODE, response.errorCode)
    }

    @Test
    fun `test getEvolutionChain returns a success evolutionChain network response`() = runBlockingTest {
        coEvery { dexDataSource.getEvolutionChain(EVOLUTION_CHAIN_URL) }.returns(evolutionChainResponse)
        every { evolutionChainResponse.responseBody }.returns(evolutionChain)
        val response = repository.getEvolutionChain(EVOLUTION_CHAIN_URL)
        coVerify {
            dexDataSource.getEvolutionChain(EVOLUTION_CHAIN_URL)
        }
        response as NetworkResponse.Success
        assertEquals(evolutionChain, response.responseBody)
    }

    @Test
    fun `test getEvolutionChain returns an error evolutionChain network response`() = runBlockingTest {
        coEvery { dexDataSource.getEvolutionChain(EVOLUTION_CHAIN_URL) }.returns(errorResponse)
        every { errorResponse.errorCode }.returns(ERROR_CODE)
        val response = repository.getEvolutionChain(EVOLUTION_CHAIN_URL)
        coVerify {
            dexDataSource.getEvolutionChain(EVOLUTION_CHAIN_URL)
        }
        response as NetworkResponse.Error
        assertEquals(ERROR_CODE, response.errorCode)
    }

    companion object {
        private const val SPRITE_URL = "https://pokeapi.co/api/v2/some_sprite_url"
        private const val SPECIES_URL = "https://pokeapi.co/api/v2/some_species_url"
        private const val EVOLUTION_CHAIN_URL = "https://pokeapi.co/api/v2/some_evo_chain_url"
        private const val ERROR_CODE = "404"
        private const val SQUIRTLE = "squirtle"
    }
}