package com.widlof.minimaldex.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape

class DrawableUtils {
    companion object {
        fun generateDrawable(context: Context, color: Int): Drawable {
            val r = 8f
            return ShapeDrawable(
                RoundRectShape(floatArrayOf(r, r, r, r, r, r, r, r), null, null)
            ).apply {
                paint.color = ColourUtils.getColour(context, color)
            }
        }
    }
}