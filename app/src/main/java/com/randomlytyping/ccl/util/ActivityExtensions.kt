@file:JvmName("LayoutUtils")

package com.randomlytyping.ccl.util

import android.support.v7.app.AppCompatActivity
import com.randomlytyping.ccl.R

// Utility methods / extensions related to activity layouts

/**
 * Sets up an activity's app bar.
 */
fun AppCompatActivity.setUpAppBar() {
  // Set up app bar.
  setSupportActionBar(findViewById(R.id.app_bar))
  supportActionBar?.run {
    setDisplayShowHomeEnabled(true)
    setDisplayHomeAsUpEnabled(true)
  }
}