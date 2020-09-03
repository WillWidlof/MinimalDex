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
    private lateinit var jsonResponse: NetworkResponse.Success<String?>

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
    fun `test getNationalDex returns an error network response for Http error`() = runBlockingTest {
        coEvery { networkRequestSender.makeJsonRequest(NATIONAL_DEX_URL) }.returns(errorResponse)
        every { errorResponse.errorCode }.returns(ERROR_CODE)
        val response = dexRemoteDataSource.getNationalDex()
        response as NetworkResponse.Error
        assertEquals(ERROR_CODE, response.errorCode)
    }

    @Test
    fun `test getNationalDex returns an error network response for null json`() = runBlockingTest {
        coEvery { networkRequestSender.makeJsonRequest(NATIONAL_DEX_URL) }.returns(jsonResponse)
        every { jsonResponse.responseBody }.returns(null)
        val response = dexRemoteDataSource.getNationalDex()
        response as NetworkResponse.Error
        assertEquals(PARSE_ERROR, response.errorCode)
    }

    @Test
    fun `test getSinglePokemonMainJson returns a success network response`() = runBlockingTest {
        coEvery { networkRequestSender.makeJsonRequest(POKEMON_URL) }.returns(jsonResponse)
        every { jsonResponse.responseBody }.returns(POKEMON_JSON)
        val response = dexRemoteDataSource.getSinglePokemonMainJson(SQUIRTLE)
        response as NetworkResponse.Success
        response.responseBody?.let {
            assertEquals(SQUIRTLE, it.name)
            assertEquals(1, it.types.size)
            assertEquals(WATER, it.types[0].type.name)
            assertEquals(1, it.moves.size)
            assertEquals("mega-punch", it.moves[0].move.name)
            assertEquals(SQUIRTLE, it.species.name)
            assertEquals(SQUIRTLE_SPECIES_URL, it.species.url)
            assertEquals(FRONT_SPRITE, it.sprites.front_default)
            assertEquals(BACK_SPRITE, it.sprites.back_default)
        }
    }

    @Test
    fun `test getSinglePokemonMainJson returns an error network response for Http error`() = runBlockingTest {
        coEvery { networkRequestSender.makeJsonRequest(POKEMON_URL) }.returns(errorResponse)
        every { errorResponse.errorCode }.returns(ERROR_CODE)
        val response = dexRemoteDataSource.getSinglePokemonMainJson(SQUIRTLE)
        response as NetworkResponse.Error
        assertEquals(ERROR_CODE, response.errorCode)
    }

    @Test
    fun `test getSinglePokemonMainJson returns an error network response for null json`() = runBlockingTest {
        coEvery { networkRequestSender.makeJsonRequest(POKEMON_URL) }.returns(jsonResponse)
        every { jsonResponse.responseBody }.returns(null)
        val response = dexRemoteDataSource.getSinglePokemonMainJson(SQUIRTLE)
        response as NetworkResponse.Error
        assertEquals(PARSE_ERROR, response.errorCode)
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
        private const val POKEMON_JSON = "{\"abilities\": [{\"ability\": {\"name\": \"torrent\", \"url\": \"https://pokeapi.co/api/v2/ability/67/\"}, \"is_hidden\": false, \"slot\": 1}, {\"ability\": {\"name\": \"rain-dish\", \"url\": \"https://pokeapi.co/api/v2/ability/44/\"}, \"is_hidden\": true, \"slot\": 3}], \"base_experience\": 63, \"forms\": [{\"name\": \"squirtle\", \"url\": \"https://pokeapi.co/api/v2/pokemon-form/7/\"}], \"game_indices\": [{\"game_index\": 177, \"version\": {\"name\": \"red\", \"url\": \"https://pokeapi.co/api/v2/version/1/\"}}, {\"game_index\": 177, \"version\": {\"name\": \"blue\", \"url\": \"https://pokeapi.co/api/v2/version/2/\"}}], \"height\": 5, \"held_items\": [], \"id\": 7, \"is_default\": true, \"location_area_encounters\": \"https://pokeapi.co/api/v2/pokemon/7/encounters\", \"moves\": [{\"move\": {\"name\": \"mega-punch\", \"url\": \"https://pokeapi.co/api/v2/move/5/\"}, \"version_group_details\": [{\"level_learned_at\": 0, \"move_learn_method\": {\"name\": \"machine\", \"url\": \"https://pokeapi.co/api/v2/move-learn-method/4/\"}, \"version_group\": {\"name\": \"red-blue\", \"url\": \"https://pokeapi.co/api/v2/version-group/1/\"}}, {\"level_learned_at\": 0, \"move_learn_method\": {\"name\": \"machine\", \"url\": \"https://pokeapi.co/api/v2/move-learn-method/4/\"}, \"version_group\": {\"name\": \"yellow\", \"url\": \"https://pokeapi.co/api/v2/version-group/2/\"}}]}], \"name\": \"squirtle\", \"order\": 10, \"species\": {\"name\": \"squirtle\", \"url\": \"https://pokeapi.co/api/v2/pokemon-species/7/\"}, \"sprites\": {\"back_default\": \"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/7.png\", \"back_female\": null, \"back_shiny\": \"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/shiny/7.png\", \"back_shiny_female\": null, \"front_default\": \"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/7.png\", \"front_female\": null, \"front_shiny\": \"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/7.png\", \"front_shiny_female\": null, \"other\": {\"dream_world\": {\"front_default\": \"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/dream-world/7.svg\", \"front_female\": null}, \"official-artwork\": {\"front_default\": \"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/7.png\"}}, \"versions\": {\"generation-viii\": {\"icons\": {\"front_default\": \"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-viii/icons/7.png\", \"front_female\": null}}}}, \"stats\": [{\"base_stat\": 44, \"effort\": 0, \"stat\": {\"name\": \"hp\", \"url\": \"https://pokeapi.co/api/v2/stat/1/\"}}, {\"base_stat\": 48, \"effort\": 0, \"stat\": {\"name\": \"attack\", \"url\": \"https://pokeapi.co/api/v2/stat/2/\"}}, {\"base_stat\": 65, \"effort\": 1, \"stat\": {\"name\": \"defense\", \"url\": \"https://pokeapi.co/api/v2/stat/3/\"}}, {\"base_stat\": 50, \"effort\": 0, \"stat\": {\"name\": \"special-attack\", \"url\": \"https://pokeapi.co/api/v2/stat/4/\"}}, {\"base_stat\": 64, \"effort\": 0, \"stat\": {\"name\": \"special-defense\", \"url\": \"https://pokeapi.co/api/v2/stat/5/\"}}, {\"base_stat\": 43, \"effort\": 0, \"stat\": {\"name\": \"speed\", \"url\": \"https://pokeapi.co/api/v2/stat/6/\"}}], \"types\": [{\"slot\": 1, \"type\": {\"name\": \"water\", \"url\": \"https://pokeapi.co/api/v2/type/11/\"}}], \"weight\": 90}"
        private const val SQUIRTLE = "squirtle"
        private const val WARTORTLE = "wartortle"
        private const val BLASTOISE = "blastoise"
        private const val SQUIRTLE_URL = "https://pokeapi.co/api/v2/pokemon/7/"
        private const val SQUIRTLE_SPECIES_URL = "https://pokeapi.co/api/v2/pokemon-species/7/"
        private const val WARTORTLE_URL = "https://pokeapi.co/api/v2/pokemon/8/"
        private const val BLASTOISE_URL = "https://pokeapi.co/api/v2/pokemon/9/"
        private const val ERROR_CODE = "404"
        private const val WATER = "water"
        private const val FRONT_SPRITE = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/7.png"
        private const val BACK_SPRITE = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/7.png"
        private const val POKEMON_URL = "https://pokeapi.co/api/v2/pokemon/squirtle"
        private const val NATIONAL_DEX_URL = "https://pokeapi.co/api/v2/pokemon?limit=2000"
        private const val PARSE_ERROR = "PARSE_ERROR"
    }
}