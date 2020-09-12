package com.widlof.minimaldex.nationaldex.data.interactor

import com.widlof.minimaldex.pokemondetails.data.model.PokemonStat
import com.widlof.minimaldex.pokemondetails.data.model.PokemonStatResponse
import io.mockk.MockKAnnotations
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetStatsInteractorTest {

    private lateinit var getStatsInteractor: GetStatsInteractor


    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        getStatsInteractor = GetStatsInteractor()
    }

    @Test
    fun `test getStats maps the response list`() {
        val list = mutableListOf<PokemonStatResponse>().apply {
            add(PokemonStatResponse(STAT, PokemonStat(HP, HP_URL, null)))
            add(PokemonStatResponse(STAT, PokemonStat(SPEED, SPEED_URL, null)))
        }
        val stats = getStatsInteractor.getStats(list)
        assertEquals(2, stats.size)
        assertEquals(HP, stats[0].name)
        assertEquals(HP_URL, stats[0].url)
        assertEquals(STAT, stats[0].value)
        assertEquals(SPEED, stats[1].name)
        assertEquals(SPEED_URL, stats[1].url)
        assertEquals(STAT, stats[1].value)
    }

    companion object {
        private const val STAT = "50"
        private const val HP = "Hp"
        private const val SPEED = "Speed"
        private const val HP_URL = "hp url"
        private const val SPEED_URL = "speed url"
    }
}