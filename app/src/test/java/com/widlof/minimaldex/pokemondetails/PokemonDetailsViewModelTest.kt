package com.widlof.minimaldex.pokemondetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.widlof.minimaldex.nationaldex.data.interactor.GetSinglePokemonInteractor
import com.widlof.minimaldex.pokemondetails.data.PokemonCache
import com.widlof.minimaldex.pokemondetails.data.model.PokemonSingle
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PokemonDetailsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var pokemonDetailsViewModel: PokemonDetailsViewModel

    @MockK
    private lateinit var getSinglePokemonInteractor: GetSinglePokemonInteractor

    @MockK
    private lateinit var pokemomSingle: PokemonSingle

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        mockkObject(PokemonCache)
        Dispatchers.setMain(Dispatchers.Unconfined)
        pokemonDetailsViewModel = PokemonDetailsViewModel(getSinglePokemonInteractor)
    }

    @Test
    fun `test init updates loading LiveData`() {
        val isLoading = pokemonDetailsViewModel.isLoadingEvolution.value
        assertEquals(false, isLoading)
    }

    @Test
    fun `test findLoadedPokemon returns a matching object`() {
        every { PokemonCache.pokemonCache[SQUIRTLE] }.returns(pokemomSingle)
        pokemonDetailsViewModel.findLoadedPokemon(SQUIRTLE)
        assertEquals(pokemomSingle, pokemonDetailsViewModel.pokemon.value)
    }

    @Test
    fun `getEvolvedForm returns a cached pokemon`() {
        every { PokemonCache.pokemonCache.containsKey(SQUIRTLE) }.returns(true)
        every { PokemonCache.pokemonCache[SQUIRTLE] }.returns(pokemomSingle)
        pokemonDetailsViewModel.getPokemonEvolvedForm(SQUIRTLE)
        val pokemon = pokemonDetailsViewModel.pokemon.value
        val isLoading = pokemonDetailsViewModel.isLoadingEvolution.value
        assertEquals(pokemomSingle, pokemon)
        assertEquals(true, isLoading)
    }

    @Test
    fun `getEvolvedForm updates LiveData with a network value`() {
        every { PokemonCache.pokemonCache.containsKey(SQUIRTLE) }.returns(false)
        coEvery { getSinglePokemonInteractor.getSinglePokemon(SQUIRTLE) }.returns(pokemomSingle)
        pokemonDetailsViewModel.getPokemonEvolvedForm(SQUIRTLE)
        val pokemon = pokemonDetailsViewModel.pokemon.value
        val isLoading = pokemonDetailsViewModel.isLoadingEvolution.value
        assertEquals(pokemomSingle, pokemon)
        assertEquals(true, isLoading)
    }

    @Test
    fun `getEvolvedForm does not update LiveData`() {
        every { PokemonCache.pokemonCache.containsKey(SQUIRTLE) }.returns(false)
        coEvery { getSinglePokemonInteractor.getSinglePokemon(SQUIRTLE) }
            .throws(NullPointerException(NULL_POINTER))
        pokemonDetailsViewModel.getPokemonEvolvedForm(SQUIRTLE)
        val pokemon = pokemonDetailsViewModel.pokemon.value
        val isLoading = pokemonDetailsViewModel.isLoadingEvolution.value
        assertEquals(null, pokemon)
        assertEquals(true, isLoading)
    }

    @Test
    fun `loadingEvolutionComplete sets loading LiveData to false`() {
        pokemonDetailsViewModel.loadingEvolutionComplete()
        val loading = pokemonDetailsViewModel.isLoadingEvolution.value
        assertEquals(false, loading)
    }

    @After
    fun after() {
        pokemonDetailsViewModel.pokemon.value = null
        pokemonDetailsViewModel.isLoadingEvolution.value = false
    }

    companion object {
        private const val SQUIRTLE = "squirtle"
        private const val NULL_POINTER = "Null Pointer"
    }
}