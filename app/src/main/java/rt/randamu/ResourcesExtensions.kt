@file:JvmName("ResourceUtils")

package rt.randamu

import android.content.res.Resources
import android.support.annotation.ArrayRes

// Utility methods / extensions related to Resources

/**
 * Retrieve an array consisting of resource IDs, a list of references to other resources.
 */
fun Resources.getResourceIdArray(@ArrayRes resId: Int): IntArray {
  val typedArray = obtainTypedArray(resId)
  val idArray = IntArray(typedArray.length())
  for (i in 0 until typedArray.length()) {
    idArray[i] = typedArray.getResourceId(i, -1)
  }
  typedArray.recycle()
  return idArray
}

