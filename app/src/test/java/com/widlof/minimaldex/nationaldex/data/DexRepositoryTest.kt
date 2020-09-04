package com.widlof.minimaldex.nationaldex.data

import android.graphics.Bitmap
import com.widlof.minimaldex.nationaldex.data.model.EvolutionChainResponse
import com.widlof.minimaldex.nationaldex.data.model.NationalDexResponse
import com.widlof.minimaldex.nationaldex.data.model.SpeciesResponse
import com.widlof.minimaldex.pokemondetails.data.model.PokemonResponse
import io.mockk.Called
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class DexRepositoryTest {

    private lateinit var repository: DexRepository

    @MockK
    private lateinit var dexDataSource: DexDataSource

    @MockK
    private lateinit var nationalDex: NationalDexResponse

    @MockK
    private lateinit var singlePokemonResponse: PokemonResponse

    @MockK
    private lateinit var speciesResponse: SpeciesResponse

    @MockK
    private lateinit var evolutionChainResponse: EvolutionChainResponse

    @MockK
    private lateinit var bitmap: Bitmap

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        repository = DexRepository(dexDataSource)
    }

    @Test
    fun `test getNationalDex returns a cached dex network response`() = runBlockingTest {
        repository.setCachedNationalDexResponse(nationalDex)
        val response = repository.getNationalDex()
        assertEquals(nationalDex, response)
        coVerify { dexDataSource wasNot Called }
    }

    @Test
    fun `test getNationalDex returns a success network response`() = runBlockingTest {
        coEvery { dexDataSource.getNationalDex() }.returns(nationalDex)
        val response = repository.getNationalDex()
        coVerify {
            dexDataSource.getNationalDex()
        }
        assertEquals(nationalDex, response)
    }

    @Test
    fun `test getNationalDex returns an error network response`() = runBlockingTest {
        coEvery { dexDataSource.getNationalDex() }.throws(
            NullPointerException(
                NATIONAL_DEX_EXCEPTION
            )
        )
        val exception = assertFailsWith<NullPointerException> {
            repository.getNationalDex()
        }
        assertEquals(NATIONAL_DEX_EXCEPTION, exception.message)
    }

    @Test
    fun `test getSinglePokemonMainJson returns a success network response`() = runBlockingTest {
        coEvery { dexDataSource.getSinglePokemonMainJson(SQUIRTLE) }.returns(singlePokemonResponse)
        val response = repository.getSinglePokemonMainJson(SQUIRTLE)
        coVerify {
            dexDataSource.getSinglePokemonMainJson(SQUIRTLE)
        }
        assertEquals(singlePokemonResponse, response)
    }

    @Test
    fun `test getSinglePokemonMainJson returns an error network response`() = runBlockingTest {
        coEvery { dexDataSource.getSinglePokemonMainJson(SQUIRTLE) }
            .throws(NullPointerException(SINGLE_POKEMON_EXCEPTION))
        val exception = assertFailsWith<NullPointerException> {
            repository.getSinglePokemonMainJson(SQUIRTLE)
        }
        assertEquals(SINGLE_POKEMON_EXCEPTION, exception.message)
    }

    @Test
    fun `test getSprite returns a success bitmap network response`() = runBlockingTest {
        coEvery { dexDataSource.getSprite(SPRITE_URL) }.returns(bitmap)
        val response = repository.getSprite(SPRITE_URL)
        coVerify {
            dexDataSource.getSprite(SPRITE_URL)
        }
        assertEquals(bitmap, response)
    }

    @Test
    fun `test getSprite returns an error bitmap network response`() = runBlockingTest {
        coEvery { dexDataSource.getSprite(SPRITE_URL) }
            .throws(NullPointerException(SPECIES_EXCEPTION))
        val exception = assertFailsWith<NullPointerException> {
            repository.getSprite(SPRITE_URL)
        }
        assertEquals(SPECIES_EXCEPTION, exception.message)
    }

    @Test
    fun `test getSpeciesBase returns a success speciesBase network response`() = runBlockingTest {
        coEvery { dexDataSource.getSpeciesBase(SPECIES_URL) }.returns(speciesResponse)
        val response = repository.getSpeciesBase(SPECIES_URL)
        coVerify {
            dexDataSource.getSpeciesBase(SPECIES_URL)
        }
        assertEquals(speciesResponse, response)
    }

    @Test
    fun `test getSprite returns an error speciesBase network response`() = runBlockingTest {
        coEvery { dexDataSource.getSpeciesBase(SPECIES_URL) }
            .throws(NullPointerException(SPRITE_EXCEPTION))
        val exception = assertFailsWith<NullPointerException> {
            repository.getSpeciesBase(SPECIES_URL)
        }
        assertEquals(SPRITE_EXCEPTION, exception.message)
    }

    @Test
    fun `test getEvolutionChain returns a success evolutionChain network response`() =
        runBlockingTest {
            coEvery { dexDataSource.getEvolutionChain(EVOLUTION_CHAIN_URL) }.returns(
                evolutionChainResponse
            )
            val response = repository.getEvolutionChain(EVOLUTION_CHAIN_URL)
            coVerify {
                dexDataSource.getEvolutionChain(EVOLUTION_CHAIN_URL)
            }
            assertEquals(evolutionChainResponse, response)
        }

    @Test
    fun `test getEvolutionChain returns an error evolutionChain network response`() =
        runBlockingTest {
            coEvery { dexDataSource.getEvolutionChain(EVOLUTION_CHAIN_URL) }
                .throws(NullPointerException(EVOLUTION_CHAIN_EXCEPTION))
            val exception = assertFailsWith<NullPointerException> {
                dexDataSource.getEvolutionChain(EVOLUTION_CHAIN_URL)
            }
            assertEquals(EVOLUTION_CHAIN_EXCEPTION, exception.message)
        }

    companion object {
        private const val SPRITE_URL = "https://pokeapi.co/api/v2/some_sprite_url"
        private const val SPECIES_URL = "https://pokeapi.co/api/v2/some_species_url"
        private const val EVOLUTION_CHAIN_URL = "https://pokeapi.co/api/v2/some_evo_chain_url"
        private const val SQUIRTLE = "squirtle"
        private const val NATIONAL_DEX_EXCEPTION = "National Dex JSON is null"
        private const val SINGLE_POKEMON_EXCEPTION = "Single Pokemon JSON is null"
        private const val SPRITE_EXCEPTION = "Sprite is null"
        private const val SPECIES_EXCEPTION = "Species Base is null"
        private const val EVOLUTION_CHAIN_EXCEPTION = "Evolution Chain JSON is null"
    }
}