package com.randomlytyping.ccl

import android.content.Context
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
import butterknife.bindView
import rt.randamu.findById
import com.randomlytyping.ccl.util.inflateInto
import com.randomlytyping.ccl.util.setUpAppBar
import rt.randamu.toConstraintSet
import rt.randamu.getResourceIdArray
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*

/**
 * Demonstration of dynamically building up a layout using [ConstraintSet].
 */
class DynamicConstraintLayoutActivity : AppCompatActivity() {

  // Layout generation
  private val inflater by lazy { LayoutInflater.from(this) }
  private val constraintLayout by bindView<ConstraintLayout>(R.id.constraint_layout)
  private val constraintSet by lazy { constraintLayout.toConstraintSet() }

  // Dimension resources
  private val rowHeight by lazy { resources.getDimensionPixelSize(R.dimen.dynamic_row_height) }
  private val elevation by lazy { resources.getDimension(R.dimen.elevation_content) }

  // Random images/text to choose
  private val images by lazy { resources.getResourceIdArray(R.array.unsplash_images) }
  private val strings by lazy { resources.getStringArray(R.array.random_words).toList() }

  // Bookkeeping
  private var indexImages = 0
  private var indexStrings = 0

  @IdRes private var lastViewId: Int = NO_ID
  @IdRes private var lastRowId: Int = PARENT_ID

  private val currentRow: MutableList<Int> = mutableListOf()

  private val randomNumber = Random(1)

  //region // Activity lifecycle

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_container_linear_layout)

    // Inflate content and bind views.
    inflateInto<ViewGroup>(R.id.linear_layout, R.layout.content_dynamic_constraint_layout)

    setUpAppBar()

    findById<View>(R.id.button_add_image).setOnClickListener { addItem(this::newImage) }
    findById<View>(R.id.button_add_text).setOnClickListener { addItem(this::newText) }
    findById<View>(R.id.button_clear).setOnClickListener {
      constraintLayout.removeAllViews()
      lastRowId = PARENT_ID
      lastViewId = NO_ID
      currentRow.clear()
      constraintSet.clone(constraintLayout)
    }
  }

  //endregion

  //region // Calligraphy bootstrapping

  override fun attachBaseContext(newBase: Context) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
  }

  //endregion

  //region // Dynamic ConstraintLayout building

  /**
   * Add item as either an item in the current row or as the single item in a whole new row.
   */
  private fun addItem(newItem: () -> View) {
    if (currentRow.isNotEmpty() && flip()) addRowItem(newItem())
    else addNewRow(newItem())
  }

  /**
   * Add given [view] as whole new row in layout.
   */
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

  /**
   * Add given [view] as constituent item of current row in layout.
   */
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

  /**
   * Remove elevation from previously added view and add to new view.
   */
  private fun toggleElevation(@IdRes newViewId: Int) {
    constraintSet.setElevation(newViewId, elevation)
    constraintSet.setElevation(lastViewId, 0f)
  }

  /**
   * Create new ImageView and set its text to the next image drawable in the list.
   */
  private fun newImage() =
      (inflater.inflate(R.layout.layout_dynamic_image, constraintLayout, false) as ImageView).apply {
        id = View.generateViewId()
        setImageResource(images[indexImages])

        // Adjust index.
        indexImages = (indexImages + 1) % images.size
      }

  /**
   * Create new TextView and set its text to the next string in the list.
   */
  private fun newText() =
      (inflater.inflate(R.layout.layout_dynamic_text, constraintLayout, false) as TextView).apply {
        id = View.generateViewId()
        text = strings[indexStrings]

        // Adjust index
        indexStrings = (indexStrings + 1) % strings.size
      }

  //endregion

  //region // Randomizer

  /**
   * Flip a coin. Used to decide between "new row" or "add to current row".
   */
  private fun flip() = randomNumber.nextInt(2) == 0

  //endregion
}
