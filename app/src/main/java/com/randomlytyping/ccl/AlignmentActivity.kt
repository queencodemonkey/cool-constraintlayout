package com.randomlytyping.ccl

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.transition.TransitionManager
import android.view.View
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import com.randomlytyping.ccl.util.inflateInto
import rt.randamu.tintBackground
import timber.log.Timber

/**
 * Examples of ways to leverage edge constraints to do interesting/useful component alignment.
 */
class AlignmentActivity : AppCompatActivity() {

  //region // Fields

  @BindView(R.id.constraint_layout) lateinit var constraintLayout: ConstraintLayout
  @BindView(R.id.ratio_group) lateinit var ratioGroup: RadioGroup
  @BindView(R.id.seek_bar) lateinit var seekBar: SeekBar

  @BindView(R.id.anchor_top) lateinit var anchorTop: View
  @BindView(R.id.anchor_bottom) lateinit var anchorBottom: View
  @BindView(R.id.anchor_start) lateinit var anchorStart: View
  @BindView(R.id.anchor_end) lateinit var anchorEnd: View

  private var anchorMinSize: Int = 0
  private var anchorMaxSize: Int = 0

  /**
   * @return A [ConstraintSet] that can be updated and applied to the current layout.
   */
  private var constraintSet = ConstraintSet()

  // Manual example fields
  @BindView(R.id.frame_anchor_top) lateinit var frameAnchorTop: View
  @BindView(R.id.frame_anchor_bottom) lateinit var frameAnchorBottom: View
  @BindView(R.id.frame_anchor_start) lateinit var frameAnchorStart: View
  @BindView(R.id.frame_anchor_end) lateinit var frameAnchorEnd: View

  private var anchorMarginHorizontal: Int = 0
  private var anchorMarginVertical: Int = 0

  //endregion

  //region // Activity lifecycle

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_container_scroll_view)

    // Inflate content and bind views.
    ButterKnife.bind(this, inflateInto<ScrollView>(R.id.scroll_view, R.layout.content_alignment))

    // Set up app bar.
    setSupportActionBar(ButterKnife.findById<Toolbar>(this, R.id.app_bar))
    supportActionBar?.also {
      it.setDisplayShowHomeEnabled(true)
      it.setDisplayHomeAsUpEnabled(true)
    }

    // Initialize constraint set
    constraintSet.clone(constraintLayout)

    // Set up anchors
    val colorAccent = ContextCompat.getColor(this, R.color.colorAccent)
    anchorTop.tintBackground(colorAccent)
    anchorBottom.tintBackground(colorAccent)
    anchorStart.tintBackground(colorAccent)
    anchorEnd.tintBackground(colorAccent)

    // Set up seek bar to scale up/down size of the fake anchors.
    anchorMinSize = resources.getDimensionPixelSize(R.dimen.alignment_anchor_min_size)
    anchorMaxSize = resources.getDimensionPixelSize(R.dimen.alignment_anchor_max_size)
    with(seekBar) {
      max = anchorMaxSize - anchorMinSize
      setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
          update()
          updateManualAnchors()
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
      })
    }

    // Set up ratio radio group and add listener to change dimension ratio of hero image
    // when clicked.
    ratioGroup.also {
      // Set the label text for each RadioButton.
      (0..it.childCount - 1).forEach { i ->
        (it.getChildAt(i) as? RadioButton)?.apply { text = toDimensionRatio(id) }
      }

      // Apply ConstraintSet for each ratio as the radio buttons are checked.
      it.setOnCheckedChangeListener { _, _ -> update(true) }

      // Initialize ConstraintSet.
      it.check(R.id.ratio_01)
    }

    // Set up frame anchors to show manual updating
    frameAnchorTop.tintBackground(colorAccent)
    frameAnchorBottom.tintBackground(colorAccent)
    frameAnchorStart.tintBackground(colorAccent)
    frameAnchorEnd.tintBackground(colorAccent)

    anchorMarginHorizontal = resources.getDimensionPixelSize(R.dimen.alignment_content_margin_horizontal)
    anchorMarginVertical = resources.getDimensionPixelSize(R.dimen.alignment_rectangle_margin_vertical)

    updateManualAnchors()
  }

  //endregion

  //region // ConstraintSet updating


  private fun update(transition: Boolean = false) {
    if (transition) {
      TransitionManager.beginDelayedTransition(constraintLayout)
    }
    updateConstraints(constraintSet, toDimensionRatio(ratioGroup.checkedRadioButtonId),
        anchorMinSize + seekBar.progress)
    constraintSet.applyTo(constraintLayout)
  }

  private fun updateConstraints(constraintSet: ConstraintSet,
                                dimensionRatio: String, anchorSize: Int) {
    Timber.d("Anchor size $anchorSize")
    constraintSet.setDimensionRatio(R.id.hero, dimensionRatio)
    resizeAnchor(constraintSet, R.id.anchor_top, anchorSize)
    resizeAnchor(constraintSet, R.id.anchor_bottom, anchorSize)
    resizeAnchor(constraintSet, R.id.anchor_start, anchorSize)
    resizeAnchor(constraintSet, R.id.anchor_end, anchorSize)
  }

  private fun resizeAnchor(constraintSet: ConstraintSet, @IdRes viewId: Int, size: Int) {
    constraintSet.constrainWidth(viewId, size)
    constraintSet.constrainHeight(viewId, size)
  }

  private fun toDimensionRatio(@IdRes radioButtonId: Int) = getString(when (radioButtonId) {
    R.id.ratio_01 -> R.string.ratio_3_1
    R.id.ratio_02 -> R.string.ratio_16_9
    R.id.ratio_03 -> R.string.ratio_1_1
    else -> R.string.ratio_3_1
  })

  //endregion

  //region // Manual anchor updates

  private fun updateManualAnchors() {
    val size = anchorMinSize + seekBar.progress
    val halfSize = Math.round(size * 0.5f)
    val horizontalMargin = resources.getDimensionPixelSize(R.dimen.content_margin_horizontal) - halfSize
    val verticalMargin = resources.getDimensionPixelSize(R.dimen.alignment_rectangle_margin_vertical) - halfSize
    updateManualAnchor(frameAnchorTop, size, verticalMargin, 0, 0, 0)
    updateManualAnchor(frameAnchorStart, size, 0, horizontalMargin, 0, 0)
    updateManualAnchor(frameAnchorBottom, size, 0, 0, verticalMargin, 0)
    updateManualAnchor(frameAnchorEnd, size, 0, 0, 0, horizontalMargin)
  }

  private fun updateManualAnchor(anchor: View, size: Int,
                                 marginTop: Int, marginStart: Int,
                                 marginBottom: Int, marginEnd: Int) {
    anchor.layoutParams = (anchor.layoutParams as FrameLayout.LayoutParams).apply {
      width = size
      height = size
      topMargin = marginTop
      bottomMargin = marginBottom
      this.marginStart = marginStart
      this.marginEnd = marginEnd
    }
  }

  // endregion
}