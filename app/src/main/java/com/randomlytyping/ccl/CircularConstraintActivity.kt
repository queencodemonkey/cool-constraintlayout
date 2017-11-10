package com.randomlytyping.ccl

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import com.randomlytyping.ccl.util.setUpAppBar
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * Example of circular constraints.
 */
class CircularConstraintActivity : AppCompatActivity() {

  //region // Activity lifecycle

  override fun attachBaseContext(newBase: Context?) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_container_linear_layout)

    // Inflate content and bind views.
    LayoutInflater.from(this)
        .inflate(R.layout.content_circular_constraint, findViewById(R.id.linear_layout))

    setUpAppBar()
  }

  //endregion
}
