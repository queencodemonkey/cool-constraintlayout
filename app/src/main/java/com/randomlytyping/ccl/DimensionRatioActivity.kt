package com.randomlytyping.ccl

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import com.randomlytyping.ccl.util.setUpAppBar

/**
 * Examples of using [ConstraintLayout]'s dimension ratio.
 */
class DimensionRatioActivity : AppCompatActivity() {

  //region // Activity lifecycle

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_container_linear_layout)

    // Inflate content and bind views.
    LayoutInflater.from(this).inflate(R.layout.content_dimension_ratio, findViewById(R.id.linear_layout))

    setUpAppBar()
  }

  //endregion
}
