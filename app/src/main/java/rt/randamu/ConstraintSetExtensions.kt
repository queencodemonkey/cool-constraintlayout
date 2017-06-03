package rt.randamu

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet

// Utility and extension methods for ConstraintSet

/**
 * Class containing static factory methods for [ConstraintSet] instances.
 */
class ConstraintSets private constructor() {
  companion object {
    /**
     * @return A [ConstraintSet] cloned from layout resource file with given [resId].
     */
    fun from(context: Context, @LayoutRes resId: Int) =
        ConstraintSet().apply { clone(context, resId) }
  }
}

/**
 * Makes changes to the receiver [ConstraintSet] via the [updates] block and applies the resulting
 * new constraints on the given [constraintLayout].
 */
inline fun ConstraintSet.update(
    constraintLayout: ConstraintLayout,
    clone: Boolean = false,
    updates: ConstraintSet.() -> Unit
) {
  if (clone) {
    clone(constraintLayout)
  }
  updates()
  applyTo(constraintLayout)
}

/**
 * @return A [ConstraintSet] cloned from the receiver.
 */
fun ConstraintLayout.toConstraintSet() = ConstraintSet().apply { clone(this@toConstraintSet) }
