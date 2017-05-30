package com.randomlytyping.ccl

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.ScrollView
import android.widget.SeekBar
import butterknife.bindView
import butterknife.bindViews
import com.randomlytyping.ccl.util.inflateInto
import com.randomlytyping.ccl.util.setUpAppBar
import com.randomlytyping.ccl.util.toConstraintSet
import com.randomlytyping.ccl.util.update
import rt.randamu.tintBackground

/**
 * Examples of ways to leverage edge constraints to do interesting/useful component alignment.
 */
class AlignmentActivity : AppCompatActivity() {

  //region // Fields
  private val anchorViewIds = arrayOf(R.id.anchor_top, R.id.anchor_bottom, R.id.anchor_start, R.id.anchor_end)

  // Views
  private val constraintLayout by bindView<ConstraintLayout>(R.id.constraint_layout)
  private val ratioGroup by bindView<RadioGroup>(R.id.ratio_group)
  private val seekBar by bindView<SeekBar>(R.id.seek_bar)
  private val allAnchors by bindViews<View>(R.id.anchor_top, R.id.anchor_bottom, R.id.anchor_start, R.id.anchor_end,
                                            R.id.manual_anchor_top, R.id.manual_anchor_bottom, R.id.manual_anchor_start, R.id.manual_anchor_end)
  private val manualAnchorTop by bindView<View>(R.id.manual_anchor_top)
  private val manualAnchorBottom by bindView<View>(R.id.manual_anchor_bottom)
  private val manualAnchorStart by bindView<View>(R.id.manual_anchor_start)
  private val manualAnchorEnd by bindView<View>(R.id.manual_anchor_end)

  // Resources
  private val anchorMinSize by lazy {
    resources.getDimensionPixelSize(R.dimen.alignment_anchor_min_size)
  }
  private val anchorMarginHorizontal by lazy {
    resources.getDimensionPixelSize(R.dimen.content_margin_horizontal)
  }
  private val anchorMarginVertical by lazy {
    resources.getDimensionPixelSize(R.dimen.alignment_rectangle_margin_vertical)
  }

  // Constraints

  // A ConstraintSet that can be updated and applied to the current layout.
  private val constraintSet by lazy { constraintLayout.toConstraintSet() }

  //endregion

  //region // Activity lifecycle

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_container_scroll_view)

    // Inflate content and bind views.
    inflateInto<ScrollView>(R.id.scroll_view, R.layout.content_alignment)

    setUpAppBar()

    // Set up anchor views.
    allAnchors.forEach { it.tintBackground(ContextCompat.getColor(this, R.color.colorAccent)) }

    // Set up seek bar to scale up/down size of the anchors.
    with(seekBar) {
      max = resources.getDimensionPixelSize(R.dimen.alignment_anchor_max_size) - anchorMinSize
      setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
          updateConstraintAnchors()
          updateManualAnchors()
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {}
        override fun onStopTrackingTouch(seekBar: SeekBar) {}
      })
    }

    // Set up ratio radio group and add listener to change dimension ratio of hero image
    // when clicked.
    with(ratioGroup) {
      // Set the label text for each RadioButton.
      (0 until childCount).forEach { i ->
        (getChildAt(i) as? RadioButton)?.apply { text = getDimensionRatio(id) }
      }

      // Apply ConstraintSet for each ratio as the radio buttons are checked.
      setOnCheckedChangeListener { _, _ -> updateConstraintAnchors(true) }

      // Initialize ConstraintSet.
      check(R.id.ratio_01)
    }
  }

  //endregion

  //region // ConstraintSet updating

  /**
   * Updates the size of constraint-positioned anchors.
   */
  private fun updateConstraintAnchors(transition: Boolean = false) {
    if (transition) {
      TransitionManager.beginDelayedTransition(constraintLayout)
    }

    // Calculate anchor size based on seek bar position.
    val anchorSize = anchorMinSize + seekBar.progress

    // Update ConstraintSet and apply to layout.
    constraintSet.update(constraintLayout) {
      setDimensionRatio(R.id.hero, getDimensionRatio(ratioGroup.checkedRadioButtonId))
      anchorViewIds.forEach {
        constrainWidth(it, anchorSize)
        constrainHeight(it, anchorSize)
      }
    }
  }

  /**
   * Translate the view ID of a ratio radio button to its corresponding label string.
   */
  private fun getDimensionRatio(@IdRes id: Int) = getString(when (id) {
    R.id.ratio_01 -> R.string.ratio_3_1
    R.id.ratio_02 -> R.string.ratio_16_9
    R.id.ratio_03 -> R.string.ratio_4_3
    else -> R.string.ratio_3_1
  })

  //endregion

  //region // Manual anchor updates

  /**
   * Update the sizes and margins of all the manually-positioned anchors via layout parameters.
   */
  private fun updateManualAnchors() {
    val size = anchorMinSize + seekBar.progress
    val halfSize = Math.round(size * 0.5f)
    val horizontalMargin = anchorMarginHorizontal - halfSize
    val verticalMargin = anchorMarginVertical - halfSize
    updateManualAnchor(manualAnchorTop, size, top = verticalMargin)
    updateManualAnchor(manualAnchorBottom, size, bottom = verticalMargin)
    updateManualAnchor(manualAnchorStart, size, start = horizontalMargin)
    updateManualAnchor(manualAnchorEnd, size, end = horizontalMargin)
  }

  /**
   * Sets the size and margins for a single manually-positioned anchor.
   */
  private fun updateManualAnchor(anchor: View, size: Int,
                                 top: Int = 0, bottom: Int = 0, start: Int = 0, end: Int = 0) {
    anchor.layoutParams = (anchor.layoutParams as ViewGroup.MarginLayoutParams).apply {
      width = size
      height = size
      topMargin = top
      bottomMargin = bottom
      marginStart = start
      marginEnd = end
    }
  }

  // endregion
}