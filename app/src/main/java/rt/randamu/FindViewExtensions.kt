@file:JvmName("Views")

package rt.randamu

import android.app.Activity
import android.support.annotation.IdRes
import android.view.View

// Utility and extension methods for views

/**
 * Copycat-ish version of ButterKnife's version of [Activity.findViewById] for inferring the
 * target type.
 */
@Suppress("UNCHECKED_CAST")
fun <T : View> Activity.findById(@IdRes id: Int)
    = findViewById(id) as T? ?: viewNotFound(resources.getResourceEntryName(id))

/**
 * Copycat-ish version of ButterKnife's version of [View.findViewById] for inferring the
 * target type.
 */
@Suppress("UNCHECKED_CAST")
fun <T : View> View.findById(@IdRes id: Int)
    = findViewById(id) as T? ?: viewNotFound(resources.getResourceEntryName(id))

/**
 * Pulled from KotterKnife implementation.
 */
private fun viewNotFound(id: String): Nothing
    = throw IllegalStateException("View with ID $id of matching type not found.")
