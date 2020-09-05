package com.widlof.minimaldex.nationaldex.data.interactor

import android.graphics.Bitmap
import com.widlof.minimaldex.nationaldex.data.DexDataSource

class GetSpriteInteractor(private val repository: DexDataSource) {
    suspend fun getSprite(url: String?): Bitmap? {
        return if (url != null) {
            repository.getSprite(url)
        } else {
            null
        }
    }
}