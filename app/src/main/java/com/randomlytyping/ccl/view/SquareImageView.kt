package com.randomlytyping.ccl.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

/**
 * Image view that always displays content as a square.
 */
class SquareImageView : ImageView {

  //region // View Constructors

  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

  //endregion

  //region // View Overrides

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    val size = Math.min(measuredWidth, measuredHeight)
    setMeasuredDimension(size, size)
  }

  //endregion
}