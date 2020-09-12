package com.widlof.minimaldex.nationaldex

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.widlof.minimaldex.nationaldex.data.DexDataSource
import com.widlof.minimaldex.nationaldex.data.DexRepositoryTest
import com.widlof.minimaldex.nationaldex.data.interactor.GetSinglePokemonInteractor
import com.widlof.minimaldex.nationaldex.data.model.NationalDexResponse
import com.widlof.minimaldex.nationaldex.data.model.PokemonListSingle
import com.widlof.minimaldex.pokemondetails.data.model.PokemonSingle
import com.widlof.minimaldex.ui.main.LoadingReasons
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class NationalDexViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var nationalDexViewModel: NationalDexViewModel

    @MockK
    private lateinit var repository: DexDataSource

    @MockK
    private lateinit var getSinglePokemonInteractor: GetSinglePokemonInteractor

    @MockK
    private lateinit var nationalDexResponse: NationalDexResponse

    @MockK
    private lateinit var pokemonList: List<PokemonListSingle>

    @MockK
    private lateinit var pokemonSingle: PokemonSingle

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(Dispatchers.Unconfined)
        nationalDexViewModel = NationalDexViewModel(repository, getSinglePokemonInteractor)
    }

    @Test
    fun `getPokemonList success response updates LiveData`() {
        coEvery { repository.getNationalDex() }.returns(nationalDexResponse)
        every { nationalDexResponse.getPokemonList() }.returns(pokemonList)
        nationalDexViewModel.getPokemonList()
        coVerify {
            repository.getNationalDex()
        }
        assertEquals(pokemonList, nationalDexViewModel.pokemonList.value)
    }

    @Test
    fun `getPokemonList error response updates LiveData`() {
        coEvery { repository.getNationalDex() }.throws(NullPointerException(NULL_POINTER))
        nationalDexViewModel.getPokemonList()
        coVerify {
            repository.getNationalDex()
        }
        assertEquals(NULL_POINTER, nationalDexViewModel.error)
    }

    @Test
    fun `getSinglePokemon success response updates LiveData`() {
        coEvery { getSinglePokemonInteractor.getSinglePokemon(SQUIRTLE) }.returns(pokemonSingle)
        nationalDexViewModel.getSinglePokemon(SQUIRTLE)
        assertEquals(true, nationalDexViewModel.isLoadingEvolution.value)
        assertEquals(pokemonSingle, nationalDexViewModel.pokemon.value)
    }

    @Test
    fun `getSinglePokemon error response updates LiveData`() = runBlockingTest {
        coEvery { getSinglePokemonInteractor.getSinglePokemon(SQUIRTLE) }
            .throws(NullPointerException(NULL_POINTER))
        val exception = assertFailsWith<NullPointerException> {
            getSinglePokemonInteractor.getSinglePokemon(SQUIRTLE)
        }
        assertEquals(NULL_POINTER, exception.message)
    }

    @Test
    fun `dexLoadComplete sets the loading LiveData to false`() {
        nationalDexViewModel.dexLoadComplete()
        assertEquals(false, nationalDexViewModel.isLoadingEvolution.value)
    }

    companion object {
        private const val NULL_POINTER = "Null Pointer"
        private const val SQUIRTLE = "squirtle"
    }
}