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
import android.widget.ImageView
import android.widget.TextView
import butterknife.bindView
import com.randomlytyping.ccl.util.setUpAppBar
import rt.randamu.getResourceIdArray
import rt.randamu.toConstraintSet
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.Random

/**
 * Demonstration of dynamically building up a layout using [ConstraintSet].
 *
 * Note: the code here is intentionally more verbose as I tend to use this
 * example for presentations where I want to explain in more detail.
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
  private val strings by lazy { resources.getStringArray(R.array.random_words) }

  // Bookkeeping
  @IdRes private var lastViewId: Int = NO_ID
  @IdRes private var lastRowId: Int = PARENT_ID

  private var indexImages = 0
  private var indexStrings = 0
  private var currentRowSize = 0

  // Coin flip
  private val randomNumber = Random(1)

  //region // Activity lifecycle

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_container_linear_layout)

    // Inflate content and bind views.
    LayoutInflater.from(this).inflate(R.layout.content_dynamic_constraint_layout, findViewById(R.id.linear_layout))

    setUpAppBar()

    findViewById<View>(R.id.button_add_image).setOnClickListener { addItem(this::newImage) }
    findViewById<View>(R.id.button_add_text).setOnClickListener { addItem(this::newText) }
    findViewById<View>(R.id.button_clear).setOnClickListener {
      constraintLayout.removeAllViews()
      lastRowId = PARENT_ID
      lastViewId = NO_ID
      currentRowSize = 0
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
    if (currentRowSize > 0 && flip()) addRowItem(newItem()) else addNewRow(newItem())
  }

  /**
   * Add given [view] as whole new row in layout.
   */
  private fun addNewRow(view: View) {
    constraintLayout.addView(view)

    if (currentRowSize > 0) {
      lastRowId = lastViewId
    }

    val id = view.id
    constraintSet.constrainWidth(id, MATCH_CONSTRAINT)
    constraintSet.constrainHeight(id, rowHeight)
    constraintSet.connect(id, TOP, lastRowId, if (lastRowId == PARENT_ID) TOP else BOTTOM)
    constraintSet.connect(id, START, PARENT_ID, START)
    constraintSet.connect(id, END, PARENT_ID, END)

    // Toggle elevation to elevate last added view.
    constraintSet.setElevation(id, elevation)
    constraintSet.setElevation(lastViewId, 0f)

    // Apply constraints.
    constraintSet.applyTo(constraintLayout)

    // Update current row count.
    currentRowSize = 1

    // Record as last view added.
    lastViewId = id
  }

  /**
   * Add given [view] as constituent item of current row in layout.
   */
  private fun addRowItem(view: View) {
    if (currentRowSize == 0) {
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
    currentRowSize++

    if (currentRowSize == 2) {
      // Once the row has two items in it, create a horizontal chain.
      constraintSet.createHorizontalChainRtl(
          PARENT_ID, START,
          PARENT_ID, END,
          intArrayOf(lastViewId, id), floatArrayOf(1f, 1f),
          CHAIN_SPREAD_INSIDE)
    } else {
      // The row already has at least two items so just add the new view to the chain.
      constraintSet.addToHorizontalChainRTL(id, lastViewId, PARENT_ID)
      constraintSet.setHorizontalWeight(id, 1f)
    }

    // Fix for bug in `setHorizontalWeight`.
    constraintSet.setHorizontalWeight(lastViewId, 1f)

    // Toggle elevation to elevate last added view.
    constraintSet.setElevation(id, elevation)
    constraintSet.setElevation(lastViewId, 0f)

    // Apply constraints.
    constraintSet.applyTo(constraintLayout)

    // Record as last view added.
    lastViewId = id
  }

  /**
   * Create new ImageView and set its text to the next image drawable in the list.
   */
  private fun newImage(): ImageView {
    val imageView =
        inflater.inflate(R.layout.layout_dynamic_image, constraintLayout, false) as ImageView
    return imageView.apply {
      id = View.generateViewId()
      setImageResource(images[indexImages])

      // Adjust index.
      indexImages = (indexImages + 1) % images.size
    }
  }

  /**
   * Create new TextView and set its text to the next string in the list.
   */
  private fun newText(): TextView {
    val textView =
        inflater.inflate(R.layout.layout_dynamic_text, constraintLayout, false) as TextView
    return textView.apply {
      id = View.generateViewId()
      text = strings[indexStrings]

      // Adjust index
      indexStrings = (indexStrings + 1) % strings.size
    }
  }

  //endregion

  //region // Randomizer

  /**
   * Flip a coin. Used to decide between "new row" or "add to current row".
   */
  private fun flip() = randomNumber.nextInt(2) == 0

  //endregion
}
