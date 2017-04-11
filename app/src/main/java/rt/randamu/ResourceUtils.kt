@file:JvmName("ResourceUtils")

package rt.randamu

import android.content.res.Resources
import android.support.annotation.ArrayRes

// Utility methods / extensions related to Resources

/**
 * Retrieve an array consisting of resource IDs, a list of references to other resources.
 */
fun Resources.getResourceIdArray(@ArrayRes idArray: Int): List<Int> {
  val typedArray = obtainTypedArray(idArray)
  val ids = (0..typedArray.length() - 1).map { i -> typedArray.getResourceId(i, -1) }
  typedArray.recycle()
  return ids
}

