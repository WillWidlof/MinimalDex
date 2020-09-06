package com.widlof.minimaldex.util

import android.content.Context
import androidx.annotation.ColorRes

class ColourUtils {
    companion object {
        fun getColour(context: Context, colour: Int): Int {
            return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                context.getColor(colour)
            } else {
                context.resources.getColor(colour)
            }
        }
    }
}