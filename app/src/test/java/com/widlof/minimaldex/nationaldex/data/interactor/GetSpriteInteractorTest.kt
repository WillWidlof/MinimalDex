package com.widlof.minimaldex.nationaldex.data.interactor

import android.graphics.Bitmap
import com.widlof.minimaldex.nationaldex.data.DexDataSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

@ExperimentalCoroutinesApi
class GetSpriteInteractorTest {

    private lateinit var getSpriteInteractor: GetSpriteInteractor

    @MockK
    private lateinit var repository: DexDataSource

    @MockK
    private lateinit var bitmap: Bitmap

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        getSpriteInteractor = GetSpriteInteractor(repository)
    }

    @Test
    fun `test getSprite returns a sprite`() = runBlockingTest {
        coEvery {
            repository.getSprite(SPRITE_URL)
        }.returns(bitmap)
        val sprite = getSpriteInteractor.getSprite(SPRITE_URL)
        assertEquals(bitmap, sprite)
    }

    @Test
    fun `test getSprite returns null for a mssing URL`() = runBlockingTest {
        val sprite = getSpriteInteractor.getSprite(null)
        assertEquals(null, sprite)
    }

    companion object {
        private const val SPRITE_URL = "url"
    }
}