@file:JvmName("DrawableUtils")

package com.randomlytyping.ccl.util

import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.ViewGroup
import butterknife.ButterKnife
import com.randomlytyping.ccl.R

// Utility methods / extensions related to activity layouts

/**
 * Inflates a given layout into a container within this activity's layout.
 */
fun <T : ViewGroup> AppCompatActivity.inflateInto(@IdRes containerId: Int,
                                                  @LayoutRes layoutResId: Int) =
    ButterKnife.findById<T>(this, containerId)?.apply {
      LayoutInflater.from(this@inflateInto).inflate(layoutResId, this)
    } ?: throw IllegalArgumentException("Activity does not have a container with id $containerId")

/**
 * Sets up an activity's app bar.
 */
fun AppCompatActivity.setUpAppBar() {
  // Set up app bar.
  setSupportActionBar(ButterKnife.findById<Toolbar>(this, R.id.app_bar))
  supportActionBar?.also {
    it.setDisplayShowHomeEnabled(true)
    it.setDisplayHomeAsUpEnabled(true)
  }
}