package com.widlof.minimaldex.nationaldex.data.interactor

import com.widlof.minimaldex.pokemondetails.data.model.PokemonMove
import com.widlof.minimaldex.pokemondetails.data.model.PokemonMoveResponse
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class GetMovesInteractorTest {

    private lateinit var getMovesInteractor: GetMovesInteractor

    @MockK
    private lateinit var pokemonMoveResponse: PokemonMoveResponse

    @MockK
    private lateinit var pokemonMove: PokemonMove

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        getMovesInteractor = GetMovesInteractor()
    }

    @Test
    fun `test getMoves adds moves`() {
        every { pokemonMoveResponse.move }.returns(pokemonMove)
        every { pokemonMove.name }.returns(BUBBLE)
        every { pokemonMove.url }.returns(BUBBLE_URL)
        val responseList = mutableListOf<PokemonMoveResponse>().apply {
            add(pokemonMoveResponse)
        }
        val moves = getMovesInteractor.getMoves(responseList)
        assertEquals(1, moves.size)
        assertEquals(BUBBLE, moves[0].name)
        assertEquals(BUBBLE_URL, moves[0].url)
    }

    companion object {
        private const val BUBBLE = "bubble"
        private const val BUBBLE_URL = "bubble_url"
    }
}