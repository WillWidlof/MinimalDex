package com.widlof.minimaldex.nationaldex.data.interactor

import android.graphics.Bitmap
import com.widlof.minimaldex.nationaldex.data.DexDataSource
import com.widlof.minimaldex.nationaldex.data.model.Species
import com.widlof.minimaldex.pokemondetails.data.PokemonCache
import com.widlof.minimaldex.pokemondetails.data.model.*
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

@ExperimentalCoroutinesApi
class GetSinglePokemonInteractorTest {

    private lateinit var getSinglePokemonInteractor: GetSinglePokemonInteractor

    @MockK
    private lateinit var repository: DexDataSource

    @MockK
    private lateinit var getSpriteInteractor: GetSpriteInteractor

    @MockK
    private lateinit var getTypesInteractor: GetTypesInteractor

    @MockK
    private lateinit var getMovesInteractor: GetMovesInteractor

    @MockK
    private lateinit var getStatsInteractor: GetStatsInteractor

    @MockK
    private lateinit var getSpeciesInteractor: GetSpeciesInteractor

    @MockK
    private lateinit var pokemonResponse: PokemonResponse

    @MockK
    private lateinit var spriteResponse: PokemonSpritesResponse

    @MockK
    private lateinit var speciesResponse: PokemonSpeciesResponse

    @MockK
    private lateinit var frontSprite: Bitmap

    @MockK
    private lateinit var backSprite: Bitmap

    @MockK
    private lateinit var species: Species

    @MockK
    private lateinit var pokemonSingle: PokemonSingle

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        getSinglePokemonInteractor = GetSinglePokemonInteractor(
            repository,
            getSpriteInteractor,
            getTypesInteractor,
            getMovesInteractor,
            getStatsInteractor,
            getSpeciesInteractor)
    }

    @Test
    fun `test getSinglePokemon returns a formatted response`() = runBlockingTest {
        assertEquals(null, PokemonCache.pokemonCache[SQUIRTLE])
        coEvery { repository.getSinglePokemonMainJson(SQUIRTLE) }.returns(pokemonResponse)
        setUpSprites()
        setUpTypes()
        setUpMoves()
        setUpStats()
        setUpSpecies()
        val pokemon = getSinglePokemonInteractor.getSinglePokemon(SQUIRTLE)
        pokemon as PokemonSingle
        `test returned pokemon values`(pokemon)
    }

    @Test
    fun `test getSinglePokemon returns a cached response`() = runBlockingTest {
        PokemonCache.pokemonCache[SQUIRTLE] = pokemonSingle
        val pokemon = getSinglePokemonInteractor.getSinglePokemon(SQUIRTLE)
        pokemon as PokemonSingle
        coVerify(exactly = 0) {
            repository.getSinglePokemonMainJson(any())
        }
        assertEquals(pokemon, pokemonSingle)
    }

    @Test
    fun `test getSinglePokemon returns a null response if the repository call fails`() = runBlockingTest {
        assertEquals(null, PokemonCache.pokemonCache[SQUIRTLE])
        coEvery { repository.getSinglePokemonMainJson(SQUIRTLE) }.returns(null)
        val pokemon = getSinglePokemonInteractor.getSinglePokemon(SQUIRTLE)
        assertEquals(null, PokemonCache.pokemonCache[SQUIRTLE])
        assertEquals(null, pokemon)
    }

    @After
    fun tearDown() {
        PokemonCache.pokemonCache.clear()
    }

    private fun `test returned pokemon values`(pokemon: PokemonSingle) {
        assertEquals(SQUIRTLE, pokemon.pokemonName)
        assertEquals(frontSprite, pokemon.normalMaleFrontSprite)
        assertEquals(backSprite, pokemon.normalMaleBackSprite)
        assertEquals(1, pokemon.typeList?.size)
        assertEquals(TYPE_WATER, pokemon.typeList?.get(0)?.name)
        assertEquals(TYPE_WATER_URL, pokemon.typeList?.get(0)?.url)
        assertEquals(1, pokemon.moveList?.size)
        assertEquals(MOVE_BUBBLE, pokemon.moveList?.get(0)?.name)
        assertEquals(MOVE_BUBBLE_URL, pokemon.moveList?.get(0)?.url)
        assertEquals(1, pokemon.statList?.size)
        assertEquals(STAT_HP, pokemon.statList?.get(0)?.name)
        assertEquals(STAT_HP_URL, pokemon.statList?.get(0)?.url)
        assertEquals("50", pokemon.statList?.get(0)?.value)
    }

    private fun setUpSpecies() {
        every { pokemonResponse.species }.returns(speciesResponse)
        coEvery { getSpeciesInteractor.getSpecies(speciesResponse) }.returns(species)
    }

    private fun setUpStats() {
        val statList = mutableListOf<PokemonStatResponse>()
        val pokemonStatList = mutableListOf<PokemonStat>().apply {
            add(PokemonStat(STAT_HP, STAT_HP_URL, "50"))
        }
        every { pokemonResponse.stats }.returns(statList)
        every { getStatsInteractor.getStats(statList) }.returns(pokemonStatList)
    }

    private fun setUpMoves() {
        val moveList = mutableListOf<PokemonMoveResponse>()
        val pokemonMoveList = mutableListOf<PokemonMove>().apply {
            add(PokemonMove(MOVE_BUBBLE, MOVE_BUBBLE_URL))
        }
        every { pokemonResponse.moves }.returns(moveList)
        every { getMovesInteractor.getMoves(moveList) }.returns(pokemonMoveList)
    }

    private fun setUpTypes() {
        val typeList = listOf<PokemonTypeResponse>()
        val pokemonTypeList = mutableListOf<PokemonType>().apply {
            add(PokemonType(TYPE_WATER, TYPE_WATER_URL))
        }
        every { pokemonResponse.types }.returns(typeList)
        every { getTypesInteractor.getTypes(typeList) }.returns(pokemonTypeList)
    }

    private fun setUpSprites() {
        every { pokemonResponse.sprites }.returns(spriteResponse)
        every { spriteResponse.front_default }.returns(FRONT_SPRITE_URL)
        every { spriteResponse.back_default }.returns(BACK_SPRITE_URL)
        coEvery { getSpriteInteractor.getSprite(FRONT_SPRITE_URL) }.returns(frontSprite)
        coEvery { getSpriteInteractor.getSprite(BACK_SPRITE_URL) }.returns(backSprite)
    }

    companion object {
        private const val SQUIRTLE = "squirtle"
        private const val FRONT_SPRITE_URL = "frontUrl"
        private const val BACK_SPRITE_URL = "backUrl"
        private const val TYPE_WATER = "water"
        private const val TYPE_WATER_URL = "url"
        private const val MOVE_BUBBLE = "bubble"
        private const val MOVE_BUBBLE_URL = "bubble_url"
        private const val STAT_HP = "hp"
        private const val STAT_HP_URL = "hp_url"
    }
}