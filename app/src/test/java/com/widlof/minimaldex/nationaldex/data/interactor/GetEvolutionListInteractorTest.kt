package com.widlof.minimaldex.nationaldex.data.interactor

import com.widlof.minimaldex.nationaldex.data.model.*
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class GetEvolutionListInteractorTest {

    private lateinit var getEvolutionListInteractor: GetEvolutionListInteractor

    @MockK
    private lateinit var evolutionChainResponse: EvolutionChainResponse

    @MockK
    private lateinit var evolvesToChild: EvolutionChainEvolvesToChild

    @MockK
    private lateinit var chain: EvolutionChainEvolvesToBase

    @MockK
    private lateinit var finalEvo: EvolutionChainSpeciesChild

    @MockK
    private lateinit var species: SpeciesChildResponse

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        every { evolutionChainResponse.chain }.returns(chain)
        getEvolutionListInteractor = GetEvolutionListInteractor()
    }

    @Test
    fun `test buildEvolutionList adds the first evolution`() {
        every { chain.species }.returns(species)
        every { species.name }.returns(SPECIES_NAME)
        every { species.url }.returns(SPECIES_URL)
        every { chain.evolves_to }.returns(null)
        val evolutionList = getEvolutionListInteractor.buildEvolutionList(evolutionChainResponse)
        assertEquals(1, evolutionList.size)
        assertEquals(SPECIES_NAME, evolutionList[0].evolutionName)
        assertEquals(SPECIES_URL, evolutionList[0].evolutionUrl)
    }

    @Test
    fun `test buildEvolutionList adds the second evolution`() {
        val list = mutableListOf<EvolutionChainEvolvesToChild>().apply {
            add(evolvesToChild)
        }
        every { chain.species }.returns(null)
        every { chain.evolves_to }.returns(list)
        every { evolvesToChild.species }.returns(species)
        every { species.name }.returns(SPECIES_NAME)
        every { species.url }.returns(SPECIES_URL)
        every { evolvesToChild.evolves_to }.returns(null)
        val evolutionList = getEvolutionListInteractor.buildEvolutionList(evolutionChainResponse)
        assertEquals(1, evolutionList.size)
        assertEquals(SPECIES_NAME, evolutionList[0].evolutionName)
        assertEquals(SPECIES_URL, evolutionList[0].evolutionUrl)
    }

    @Test
    fun `test buildEvolutionList adds the third evolution`() {
        val secondEvoList = mutableListOf<EvolutionChainEvolvesToChild>().apply {
            add(evolvesToChild)
        }
        every { chain.species }.returns(null)
        every { chain.evolves_to }.returns(secondEvoList)
        every { evolvesToChild.species }.returns(null)

        val finalEvoList = mutableListOf<EvolutionChainSpeciesChild>().apply {
            add(finalEvo)
        }
        every { evolvesToChild.evolves_to }.returns(finalEvoList)
        every { finalEvo.species }.returns(species)
        every { species.name }.returns(SPECIES_NAME)
        every { species.url }.returns(SPECIES_URL)
        val evolutionList = getEvolutionListInteractor.buildEvolutionList(evolutionChainResponse)
        assertEquals(1, evolutionList.size)
        assertEquals(SPECIES_NAME, evolutionList[0].evolutionName)
        assertEquals(SPECIES_URL, evolutionList[0].evolutionUrl)
    }

    companion object {
        private const val SPECIES_NAME = "squirtle"
        private const val SPECIES_URL = "squirtle_url"
    }
}