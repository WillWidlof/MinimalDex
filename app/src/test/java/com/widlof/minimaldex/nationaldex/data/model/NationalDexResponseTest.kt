package com.widlof.minimaldex.nationaldex.data.model

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class NationalDexResponseTest {

    private lateinit var nationalDexResponse: NationalDexResponse

    @MockK
    private lateinit var pokemonListSingle: PokemonListSingle

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun `test getPokemonList`() {
        val list = mutableListOf<PokemonListSingle>().apply {
            add(pokemonListSingle)
            add(pokemonListSingle)
        }
        nationalDexResponse = NationalDexResponse(list)
        assertEquals(list, nationalDexResponse.getPokemonList())
    }
}