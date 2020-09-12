package com.widlof.minimaldex.nationaldex.data.interactor

import com.widlof.minimaldex.pokemondetails.data.model.PokemonType
import com.widlof.minimaldex.pokemondetails.data.model.PokemonTypeResponse
import io.mockk.MockKAnnotations
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class GetTypesInteractorTest {

    private lateinit var getTypesInteractor: GetTypesInteractor

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        getTypesInteractor = GetTypesInteractor()
    }

    @Test
    fun `test getTypes maps the types response`() {
        val list = mutableListOf<PokemonTypeResponse>().apply {
            add(PokemonTypeResponse(PokemonType(WATER, WATER_URL)))
            add(PokemonTypeResponse(PokemonType(GROUND, GROUND_URL)))
        }
        val types = getTypesInteractor.getTypes(list)
        assertEquals(2, types.size)
        assertEquals(WATER, types[0].name)
        assertEquals(WATER_URL, types[0].url)
        assertEquals(GROUND, types[1].name)
        assertEquals(GROUND_URL, types[1].url)
    }

    companion object {
        private const val WATER = "water"
        private const val WATER_URL = "water url"
        private const val GROUND = "ground"
        private const val GROUND_URL = "ground url"
    }
}