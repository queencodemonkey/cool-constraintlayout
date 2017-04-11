package com.randomlytyping.ccl

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.constraint.ConstraintSet.*
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.View.NO_ID
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.randomlytyping.ccl.util.inflateInto
import com.randomlytyping.ccl.util.setUpAppBar
import rt.randamu.getResourceIdArray
import java.util.*

/**
 * Activity for example demonstrating dynamically building up a layout using [ConstraintSet].
 */
class DynamicConstraintLayoutActivity : AppCompatActivity() {

  companion object {
    private enum class Content {
      IMAGE,
      TEXT
    }
  }

  @BindView(R.id.constraint_layout) internal lateinit var constraintLayout: ConstraintLayout

  // Layout generation
  private val constraintSet: ConstraintSet = ConstraintSet()
  private lateinit var inflater: LayoutInflater
  private var rowHeight = 0

  @IdRes private var lastViewId: Int = NO_ID
  @IdRes private var lastRowId: Int = PARENT_ID
  private var currentRow: LinkedList<Int> = LinkedList()

  // Random images/text to choose
  private lateinit var images: List<Int>
  private lateinit var strings: List<String>

  private var indexImages = 0
  private var indexStrings = 0

  private val randomNumber = Random(1)

  //region // Activity lifecycle

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_container_linear_layout)

    // Inflate content and bind views.
    ButterKnife.bind(this, inflateInto<ViewGroup>(R.id.linear_layout, R.layout.content_dynamic_constraint_layout))

    setUpAppBar()

    // Initialize constraint set.
    constraintSet.clone(constraintLayout)

    // Initialize layout builders.
    inflater = LayoutInflater.from(this)
    rowHeight = resources.getDimensionPixelSize(R.dimen.dynamic_row_height)

    // Initialize data…
    // Get random images.
    images = resources.getResourceIdArray(R.array.unsplash_images)
    // Get random strings.
    strings = resources.getStringArray(R.array.random_words).toList()
  }

  //endregion

  //region // Dynamic ConstraintLayout building

  private fun addNewRow(view: View) {
    constraintLayout.addView(view)

    if (currentRow.isNotEmpty()) {
      lastRowId = lastViewId
    }

    val id = view.id
    constraintSet.constrainWidth(id, MATCH_CONSTRAINT)
    constraintSet.constrainHeight(id, rowHeight)
    constraintSet.connect(id, TOP, lastRowId, if (lastRowId == PARENT_ID) TOP else BOTTOM)
    constraintSet.connect(id, START, PARENT_ID, START)
    constraintSet.connect(id, END, PARENT_ID, END)

    // Toggle elevation to elevate last added view.
    toggleElevation(id)

    // Apply constraints.
    constraintSet.applyTo(constraintLayout)

    // Update current row.
    currentRow.clear()
    currentRow.add(id)
    // Record as last view added.
    lastViewId = id
  }

  private fun addRowItem(view: View) {
    if (currentRow.isEmpty()) {
      addNewRow(view)
      return
    }

    constraintLayout.addView(view)

    val id = view.id

    // Initialize width and height of new view.
    constraintSet.constrainWidth(id, MATCH_CONSTRAINT)
    constraintSet.constrainHeight(id, rowHeight)

    // Constrain new view vertically.
    constraintSet.connect(id, TOP, lastRowId, if (lastRowId == PARENT_ID) TOP else BOTTOM)

    // Update current row.
    currentRow.add(id)
    // Re-create horizontal chain with other row items.
    constraintSet.createHorizontalChainRtl(
        PARENT_ID, START,
        PARENT_ID, END,
        currentRow.toIntArray(), FloatArray(currentRow.size, { 1f }),
        CHAIN_SPREAD_INSIDE)

    // Fix for bug in `setHorizontalWeight`.
    constraintSet.setHorizontalWeight(lastViewId, 1f)

    // Toggle elevation to elevate last added view.
    toggleElevation(id)

    // Apply constraints.
    constraintSet.applyTo(constraintLayout)

    // Record as last view added.
    lastViewId = id
  }

  private fun toggleElevation(@IdRes newViewId: Int) {
    constraintSet.setElevation(newViewId, resources.getDimension(R.dimen.elevation_content))
    constraintSet.setElevation(lastViewId, 0f)
  }


  private fun newItem(content: Content) = when (content) {
    Content.IMAGE -> newImage()
    Content.TEXT -> newText()
  }

  private fun newImage(): ImageView {
    // Create new ImageView and set its text to the next image drawable in the list.
    val imageView: ImageView = inflater.inflate(R.layout.layout_dynamic_image, constraintLayout, false) as ImageView
    imageView.id = View.generateViewId()
    imageView.setImageResource(images[indexImages])

    // Adjust index.
    indexImages = (indexImages + 1) % images.size
    return imageView
  }

  private fun newText(): TextView {
    // Create new TextView and set its text to the next string in the list.
    val textView: TextView = inflater.inflate(R.layout.layout_dynamic_text, constraintLayout, false) as TextView
    textView.id = View.generateViewId()
    textView.text = strings[indexStrings]

    // Adjust index
    indexStrings = (indexStrings + 1) % strings.size
    return textView
  }

  //endregion

  //region // Listeners

  @OnClick(R.id.button_add_image, R.id.button_add_text)
  internal fun onAddClick(view: View) {
    val newView = newItem(when (view.id) {
      R.id.button_add_image -> Content.IMAGE
      R.id.button_add_text -> Content.TEXT
      else -> throw IllegalArgumentException("Unhandled view ID: $view.id")
    })
    if (currentRow.isNotEmpty() && flip()) {
      addRowItem(newView)
    } else {
      addNewRow(newView)
    }
  }

  @OnClick(R.id.button_clear)
  internal fun onClearClick() {
    constraintLayout.removeAllViews()
    lastRowId = PARENT_ID
    lastViewId = NO_ID
    currentRow.clear()
    constraintSet.clone(constraintLayout)
  }

  //endregion

  //region // Randomizer

  private fun flip() = randomNumber.nextInt(2) == 0

  //endregion
}
