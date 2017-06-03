@file:JvmName("LayoutUtils")

package com.randomlytyping.ccl.util

import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.randomlytyping.ccl.R
import rt.randamu.findById

// Utility methods / extensions related to activity layouts

/**
 * Inflates a given layout into a container within this activity's layout.
 */
fun <T : ViewGroup> AppCompatActivity.inflateInto(@IdRes containerId: Int, @LayoutRes layoutResId: Int) =
    findById<T>(containerId).apply {
      LayoutInflater.from(this@inflateInto).inflate(layoutResId, this)
    }

/**
 * Sets up an activity's app bar.
 */
fun AppCompatActivity.setUpAppBar() {
  // Set up app bar.
  setSupportActionBar(findById(R.id.app_bar))
  supportActionBar?.run {
    setDisplayShowHomeEnabled(true)
    setDisplayHomeAsUpEnabled(true)
  }
}