@file:JvmName("DrawableUtils")

package rt.randamu

import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.v4.graphics.drawable.DrawableCompat
import android.view.View

// Utility methods / extensions related to Drawables

/**
 * Tint this view's background with a given color.
 *
 * Utilizes [DrawableCompat] methods.
 */
fun View.tintBackground(@ColorInt tint: Int): Drawable {
  val drawable = DrawableCompat.wrap(background)
  DrawableCompat.setTint(drawable, tint)
  background = drawable
  return drawable
}