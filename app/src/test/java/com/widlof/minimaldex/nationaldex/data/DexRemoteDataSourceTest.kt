package com.widlof.minimaldex.nationaldex.data

import android.graphics.Bitmap
import com.widlof.minimaldex.network.NetworkRequestSender
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class DexRemoteDataSourceTest {

    @get:Rule
    var expectedException: ExpectedException = ExpectedException.none()

    private lateinit var dexRemoteDataSource: DexRemoteDataSource

    @MockK
    private lateinit var networkRequestSender: NetworkRequestSender

    @MockK
    private lateinit var bitmap: Bitmap

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        dexRemoteDataSource = DexRemoteDataSource(Dispatchers.Unconfined, networkRequestSender)
    }

    @Test
    fun `test getNationalDex returns a success network response`() = runBlockingTest {
        coEvery { networkRequestSender.makeJsonRequest(NATIONAL_DEX_URL) }.returns(NATIONAL_DEX_JSON)
        val response = dexRemoteDataSource.getNationalDex()
        response?.let {
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
        coEvery { networkRequestSender.makeJsonRequest(NATIONAL_DEX_URL) }
            .throws(NullPointerException(NULL_POINTER))
        val exception = assertFailsWith<NullPointerException> {
            dexRemoteDataSource.getNationalDex()
        }
        assertEquals(NULL_POINTER, exception.message)
    }

    @Test
    fun `test getNationalDex returns an error network response for null json`() = runBlockingTest {
        coEvery { networkRequestSender.makeJsonRequest(NATIONAL_DEX_URL) }
            .returns(null)
        val exception = assertFailsWith<NullPointerException> {
            dexRemoteDataSource.getNationalDex()
        }
        assertEquals(NATIONAL_DEX_EXCEPTION, exception.message)
    }

    @Test
    fun `test getSinglePokemonMainJson returns a success network response`() = runBlockingTest {
        coEvery { networkRequestSender.makeJsonRequest(POKEMON_URL) }.returns(POKEMON_JSON)
        val response = dexRemoteDataSource.getSinglePokemonMainJson(SQUIRTLE)
        response?.let {
            assertEquals(SQUIRTLE, it.name)
            assertEquals(1, it.types.size)
            assertEquals(WATER, it.types[0].type.name)
            assertEquals(1, it.moves.size)
            assertEquals(MEGA_PUNCH, it.moves[0].move.name)
            assertEquals(SQUIRTLE, it.species.name)
            assertEquals(SQUIRTLE_SPECIES_URL, it.species.url)
            assertEquals(FRONT_SPRITE, it.sprites.front_default)
            assertEquals(BACK_SPRITE, it.sprites.back_default)
        }
    }

    @Test
    fun `test getSinglePokemonMainJson returns an error network response for Http error`() =
        runBlockingTest {
            coEvery { networkRequestSender.makeJsonRequest(POKEMON_URL) }
                .throws(NullPointerException(NULL_POINTER))
            val exception = assertFailsWith<NullPointerException> {
                dexRemoteDataSource.getSinglePokemonMainJson(SQUIRTLE)
            }
            assertEquals(NULL_POINTER, exception.message)
        }

    @Test
    fun `test getSinglePokemonMainJson returns an error network response for null json`() =
        runBlockingTest {
            coEvery { networkRequestSender.makeJsonRequest(POKEMON_URL) }
                .returns(null)
            val exception = assertFailsWith<NullPointerException> {
                dexRemoteDataSource.getSinglePokemonMainJson(SQUIRTLE)
            }
            assertEquals(SINGLE_POKEMON_EXCEPTION, exception.message)
        }

    @Test
    fun `test getSprite returns a success network response`() = runBlockingTest {
        coEvery { networkRequestSender.makeImageRequest(FRONT_SPRITE) }.returns(bitmap)
        val response = dexRemoteDataSource.getSprite(FRONT_SPRITE)
        assertEquals(bitmap, response)
    }

    @Test
    fun `test getSprite returns an error network response for Http error`() =
        runBlockingTest {
            coEvery { networkRequestSender.makeImageRequest(FRONT_SPRITE) }.throws(
                NullPointerException(NULL_POINTER)
            )
            val exception = assertFailsWith<NullPointerException> {
                dexRemoteDataSource.getSprite(FRONT_SPRITE)
            }
            assertEquals(NULL_POINTER, exception.message)
        }

    @Test
    fun `test getSprite returns an error network response for a parse error`() =
        runBlockingTest {
            coEvery { networkRequestSender.makeImageRequest(FRONT_SPRITE) }.returns(null)
            val exception = assertFailsWith<NullPointerException> {
                dexRemoteDataSource.getSprite(FRONT_SPRITE)
            }
            assertEquals(SPRITE_EXCEPTION, exception.message)
        }

    @Test
    fun `test getSpeciesBase returns a sucess network response`() = runBlockingTest {
        coEvery { networkRequestSender.makeJsonRequest(SQUIRTLE_SPECIES_URL) }
            .returns(SPECIES_JSON)
        val response = dexRemoteDataSource.getSpeciesBase(SQUIRTLE_SPECIES_URL)
        response?.let {
            assertEquals(70, it.base_happiness)
            assertEquals(45, it.capture_rate)
            assertEquals(SQUIRTLE_EVOLUTION_URL, it.evolution_chain.url)
        }
    }

    @Test
    fun `test getSpeciesBase returns an error network response for Http error`() = runBlockingTest {
        coEvery { networkRequestSender.makeJsonRequest(SQUIRTLE_SPECIES_URL) }.throws(
            NullPointerException(NULL_POINTER)
        )
        val exception = assertFailsWith<NullPointerException> {
            dexRemoteDataSource.getSpeciesBase(SQUIRTLE_SPECIES_URL)
        }
        assertEquals(NULL_POINTER, exception.message)
    }

    @Test
    fun `test getSpeciesBase returns an error network response for a parse error`() = runBlockingTest {
        coEvery { networkRequestSender.makeJsonRequest(SQUIRTLE_SPECIES_URL) }.returns(null)
        val exception = assertFailsWith<NullPointerException> {
            dexRemoteDataSource.getSpeciesBase(SQUIRTLE_SPECIES_URL)
        }
        assertEquals(SPECIES_EXCEPTION, exception.message)
    }

    @Test
    fun `test getEvolutionChain returns a sucess network response`() = runBlockingTest {
        coEvery { networkRequestSender.makeJsonRequest(SQUIRTLE_EVOLUTION_URL) }
            .returns(EVOLUTION_CHAIN_JSON)
        val response = dexRemoteDataSource.getEvolutionChain(SQUIRTLE_EVOLUTION_URL)
        response?.let {
            assertEquals(SQUIRTLE, it.chain.species?.name)
            assertEquals(SQUIRTLE_SPECIES_URL, it.chain.species?.url)
        }
    }

    @Test
    fun `test getEvolutionChain returns an error network response for Http error`() = runBlockingTest {
        coEvery { networkRequestSender.makeJsonRequest(SQUIRTLE_EVOLUTION_URL) }.throws(
            NullPointerException(NULL_POINTER)
        )
        val exception = assertFailsWith<NullPointerException> {
            dexRemoteDataSource.getEvolutionChain(SQUIRTLE_EVOLUTION_URL)
        }
        assertEquals(NULL_POINTER, exception.message)
    }

    @Test
    fun `test getEvolutionChain returns an error network response for a parse error`() = runBlockingTest {
        coEvery { networkRequestSender.makeJsonRequest(SQUIRTLE_EVOLUTION_URL) }.returns(null)

        val exception = assertFailsWith<NullPointerException> {
            dexRemoteDataSource.getEvolutionChain(SQUIRTLE_EVOLUTION_URL)
        }
        assertEquals(EVOLUTION_CHAIN_EXCEPTION, exception.message)
    }

    companion object {
        private const val NATIONAL_DEX_JSON =
            "{\"count\": 1050, \"next\": \"https://pokeapi.co/api/v2/pokemon?offset=20&limit=20\", \"previous\": null, \"results\": [{\"name\": \"squirtle\", \"url\": \"https://pokeapi.co/api/v2/pokemon/7/\"}, {\"name\": \"wartortle\", \"url\": \"https://pokeapi.co/api/v2/pokemon/8/\"}, {\"name\": \"blastoise\", \"url\": \"https://pokeapi.co/api/v2/pokemon/9/\"}]}"
        private const val POKEMON_JSON =
            "{\"abilities\": [{\"ability\": {\"name\": \"torrent\", \"url\": \"https://pokeapi.co/api/v2/ability/67/\"}, \"is_hidden\": false, \"slot\": 1}, {\"ability\": {\"name\": \"rain-dish\", \"url\": \"https://pokeapi.co/api/v2/ability/44/\"}, \"is_hidden\": true, \"slot\": 3}], \"base_experience\": 63, \"forms\": [{\"name\": \"squirtle\", \"url\": \"https://pokeapi.co/api/v2/pokemon-form/7/\"}], \"game_indices\": [{\"game_index\": 177, \"version\": {\"name\": \"red\", \"url\": \"https://pokeapi.co/api/v2/version/1/\"}}, {\"game_index\": 177, \"version\": {\"name\": \"blue\", \"url\": \"https://pokeapi.co/api/v2/version/2/\"}}], \"height\": 5, \"held_items\": [], \"id\": 7, \"is_default\": true, \"location_area_encounters\": \"https://pokeapi.co/api/v2/pokemon/7/encounters\", \"moves\": [{\"move\": {\"name\": \"mega-punch\", \"url\": \"https://pokeapi.co/api/v2/move/5/\"}, \"version_group_details\": [{\"level_learned_at\": 0, \"move_learn_method\": {\"name\": \"machine\", \"url\": \"https://pokeapi.co/api/v2/move-learn-method/4/\"}, \"version_group\": {\"name\": \"red-blue\", \"url\": \"https://pokeapi.co/api/v2/version-group/1/\"}}, {\"level_learned_at\": 0, \"move_learn_method\": {\"name\": \"machine\", \"url\": \"https://pokeapi.co/api/v2/move-learn-method/4/\"}, \"version_group\": {\"name\": \"yellow\", \"url\": \"https://pokeapi.co/api/v2/version-group/2/\"}}]}], \"name\": \"squirtle\", \"order\": 10, \"species\": {\"name\": \"squirtle\", \"url\": \"https://pokeapi.co/api/v2/pokemon-species/7/\"}, \"sprites\": {\"back_default\": \"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/7.png\", \"back_female\": null, \"back_shiny\": \"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/shiny/7.png\", \"back_shiny_female\": null, \"front_default\": \"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/7.png\", \"front_female\": null, \"front_shiny\": \"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/7.png\", \"front_shiny_female\": null, \"other\": {\"dream_world\": {\"front_default\": \"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/dream-world/7.svg\", \"front_female\": null}, \"official-artwork\": {\"front_default\": \"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/7.png\"}}, \"versions\": {\"generation-viii\": {\"icons\": {\"front_default\": \"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-viii/icons/7.png\", \"front_female\": null}}}}, \"stats\": [{\"base_stat\": 44, \"effort\": 0, \"stat\": {\"name\": \"hp\", \"url\": \"https://pokeapi.co/api/v2/stat/1/\"}}, {\"base_stat\": 48, \"effort\": 0, \"stat\": {\"name\": \"attack\", \"url\": \"https://pokeapi.co/api/v2/stat/2/\"}}, {\"base_stat\": 65, \"effort\": 1, \"stat\": {\"name\": \"defense\", \"url\": \"https://pokeapi.co/api/v2/stat/3/\"}}, {\"base_stat\": 50, \"effort\": 0, \"stat\": {\"name\": \"special-attack\", \"url\": \"https://pokeapi.co/api/v2/stat/4/\"}}, {\"base_stat\": 64, \"effort\": 0, \"stat\": {\"name\": \"special-defense\", \"url\": \"https://pokeapi.co/api/v2/stat/5/\"}}, {\"base_stat\": 43, \"effort\": 0, \"stat\": {\"name\": \"speed\", \"url\": \"https://pokeapi.co/api/v2/stat/6/\"}}], \"types\": [{\"slot\": 1, \"type\": {\"name\": \"water\", \"url\": \"https://pokeapi.co/api/v2/type/11/\"}}], \"weight\": 90}"
        private const val SPECIES_JSON =
            "{\"base_happiness\": 70, \"capture_rate\": 45, \"color\": {\"name\": \"blue\", \"url\": \"https://pokeapi.co/api/v2/pokemon-color/2/\"}, \"egg_groups\": [{\"name\": \"monster\", \"url\": \"https://pokeapi.co/api/v2/egg-group/1/\"}, {\"name\": \"water1\", \"url\": \"https://pokeapi.co/api/v2/egg-group/2/\"}], \"evolution_chain\": {\"url\": \"https://pokeapi.co/api/v2/evolution-chain/3/\"}, \"evolves_from_species\": null, \"flavor_text_entries\": [{\"flavor_text\": \"Shoots water at\\nprey while in the\\nwater.\\fWithdraws into\\nits shell when in\\ndanger.\", \"language\": {\"name\": \"en\", \"url\": \"https://pokeapi.co/api/v2/language/9/\"}, \"version\": {\"name\": \"yellow\", \"url\": \"https://pokeapi.co/api/v2/version/3/\"}}], \"generation\": {\"name\": \"generation-i\", \"url\": \"https://pokeapi.co/api/v2/generation/1/\"}, \"growth_rate\": {\"name\": \"medium-slow\", \"url\": \"https://pokeapi.co/api/v2/growth-rate/4/\"}, \"habitat\": {\"name\": \"waters-edge\", \"url\": \"https://pokeapi.co/api/v2/pokemon-habitat/9/\"}, \"has_gender_differences\": false, \"hatch_counter\": 20, \"id\": 7, \"is_baby\": false, \"is_legendary\": false, \"is_mythical\": false, \"name\": \"squirtle\", \"names\": [{\"language\": {\"name\": \"ja-Hrkt\", \"url\": \"https://pokeapi.co/api/v2/language/1/\"}, \"name\": \"ゼニガメ\"}, {\"language\": {\"name\": \"roomaji\", \"url\": \"https://pokeapi.co/api/v2/language/2/\"}, \"name\": \"Zenigame\"}, {\"language\": {\"name\": \"ko\", \"url\": \"https://pokeapi.co/api/v2/language/3/\"}, \"name\": \"꼬부기\"}, {\"language\": {\"name\": \"zh-Hant\", \"url\": \"https://pokeapi.co/api/v2/language/4/\"}, \"name\": \"傑尼龜\"}, {\"language\": {\"name\": \"fr\", \"url\": \"https://pokeapi.co/api/v2/language/5/\"}, \"name\": \"Carapuce\"}, {\"language\": {\"name\": \"de\", \"url\": \"https://pokeapi.co/api/v2/language/6/\"}, \"name\": \"Schiggy\"}, {\"language\": {\"name\": \"es\", \"url\": \"https://pokeapi.co/api/v2/language/7/\"}, \"name\": \"Squirtle\"}, {\"language\": {\"name\": \"it\", \"url\": \"https://pokeapi.co/api/v2/language/8/\"}, \"name\": \"Squirtle\"}, {\"language\": {\"name\": \"en\", \"url\": \"https://pokeapi.co/api/v2/language/9/\"}, \"name\": \"Squirtle\"}, {\"language\": {\"name\": \"ja\", \"url\": \"https://pokeapi.co/api/v2/language/11/\"}, \"name\": \"ゼニガメ\"}, {\"language\": {\"name\": \"zh-Hans\", \"url\": \"https://pokeapi.co/api/v2/language/12/\"}, \"name\": \"杰尼龟\"}], \"order\": 7, \"pal_park_encounters\": [{\"area\": {\"name\": \"pond\", \"url\": \"https://pokeapi.co/api/v2/pal-park-area/4/\"}, \"base_score\": 50, \"rate\": 30}], \"pokedex_numbers\": [{\"entry_number\": 7, \"pokedex\": {\"name\": \"national\", \"url\": \"https://pokeapi.co/api/v2/pokedex/1/\"}}, {\"entry_number\": 7, \"pokedex\": {\"name\": \"kanto\", \"url\": \"https://pokeapi.co/api/v2/pokedex/2/\"}}, {\"entry_number\": 232, \"pokedex\": {\"name\": \"original-johto\", \"url\": \"https://pokeapi.co/api/v2/pokedex/3/\"}}, {\"entry_number\": 237, \"pokedex\": {\"name\": \"updated-johto\", \"url\": \"https://pokeapi.co/api/v2/pokedex/7/\"}}, {\"entry_number\": 86, \"pokedex\": {\"name\": \"kalos-central\", \"url\": \"https://pokeapi.co/api/v2/pokedex/12/\"}}], \"shape\": {\"name\": \"upright\", \"url\": \"https://pokeapi.co/api/v2/pokemon-shape/6/\"}, \"varieties\": [{\"is_default\": true, \"pokemon\": {\"name\": \"squirtle\", \"url\": \"https://pokeapi.co/api/v2/pokemon/7/\"}}]}"
        private const val EVOLUTION_CHAIN_JSON = "{\"baby_trigger_item\":null,\"chain\":{\"evolution_details\":[],\"evolves_to\":[{\"evolution_details\":[{\"gender\":null,\"held_item\":null,\"item\":null,\"known_move\":null,\"known_move_type\":null,\"location\":null,\"min_affection\":null,\"min_beauty\":null,\"min_happiness\":null,\"min_level\":16,\"needs_overworld_rain\":false,\"party_species\":null,\"party_type\":null,\"relative_physical_stats\":null,\"time_of_day\":\"\",\"trade_species\":null,\"trigger\":{\"name\":\"level-up\",\"url\":\"https://pokeapi.co/api/v2/evolution-trigger/1/\"},\"turn_upside_down\":false}],\"evolves_to\":[{\"evolution_details\":[{\"gender\":null,\"held_item\":null,\"item\":null,\"known_move\":null,\"known_move_type\":null,\"location\":null,\"min_affection\":null,\"min_beauty\":null,\"min_happiness\":null,\"min_level\":36,\"needs_overworld_rain\":false,\"party_species\":null,\"party_type\":null,\"relative_physical_stats\":null,\"time_of_day\":\"\",\"trade_species\":null,\"trigger\":{\"name\":\"level-up\",\"url\":\"https://pokeapi.co/api/v2/evolution-trigger/1/\"},\"turn_upside_down\":false}],\"evolves_to\":[],\"is_baby\":false,\"species\":{\"name\":\"blastoise\",\"url\":\"https://pokeapi.co/api/v2/pokemon-species/9/\"}}],\"is_baby\":false,\"species\":{\"name\":\"wartortle\",\"url\":\"https://pokeapi.co/api/v2/pokemon-species/8/\"}}],\"is_baby\":false,\"species\":{\"name\":\"squirtle\",\"url\":\"https://pokeapi.co/api/v2/pokemon-species/7/\"}},\"id\":3}"
        private const val SQUIRTLE = "squirtle"
        private const val WARTORTLE = "wartortle"
        private const val BLASTOISE = "blastoise"
        private const val SQUIRTLE_URL = "https://pokeapi.co/api/v2/pokemon/7/"
        private const val SQUIRTLE_SPECIES_URL = "https://pokeapi.co/api/v2/pokemon-species/7/"
        private const val SQUIRTLE_EVOLUTION_URL = "https://pokeapi.co/api/v2/evolution-chain/3/"
        private const val WARTORTLE_URL = "https://pokeapi.co/api/v2/pokemon/8/"
        private const val BLASTOISE_URL = "https://pokeapi.co/api/v2/pokemon/9/"
        private const val WATER = "water"
        private const val FRONT_SPRITE =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/7.png"
        private const val BACK_SPRITE =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/7.png"
        private const val POKEMON_URL = "https://pokeapi.co/api/v2/pokemon/squirtle"
        private const val NATIONAL_DEX_URL = "https://pokeapi.co/api/v2/pokemon?limit=2000"
        private const val MEGA_PUNCH = "mega-punch"
        private const val NULL_POINTER = "NPE"
        private const val NATIONAL_DEX_EXCEPTION = "National Dex JSON is null"
        private const val SINGLE_POKEMON_EXCEPTION = "Single Pokemon JSON is null"
        private const val SPRITE_EXCEPTION = "Sprite is null"
        private const val SPECIES_EXCEPTION = "Species Base is null"
        private const val EVOLUTION_CHAIN_EXCEPTION = "Evolution Chain JSON is null"
    }
}