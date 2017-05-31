package com.randomlytyping.ccl

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.transition.TransitionManager
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import butterknife.bindView
import rt.randamu.ConstraintSets
import com.randomlytyping.ccl.util.inflateInto
import com.randomlytyping.ccl.util.setUpAppBar
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * Examples of ConstraintSet including transitioning between sets.
 */
class ConstraintSetActivity : AppCompatActivity() {

  private val constraintLayout by bindView<ConstraintLayout>(R.id.constraint_layout)
  private val constraintSet01 by lazy { ConstraintSets.from(this, R.layout.content_constraintset_01) }
  private val constraintSet02 by lazy { ConstraintSets.from(this, R.layout.content_constraintset_02) }

  private var original = true

  //region // Activity lifecycle

  override fun attachBaseContext(newBase: Context?) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_container_linear_layout)

    // Inflate content and bind views.
    inflateInto<ViewGroup>(R.id.linear_layout, R.layout.content_constraintset_01)

    setUpAppBar()
  }

  // endregion

  //region // Action item handling

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.constraint_set, menu)
    DrawableCompat.setTint(menu.findItem(R.id.action_swap_constraint_set).icon, Color.WHITE)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.action_swap_constraint_set -> {
        TransitionManager.beginDelayedTransition(constraintLayout)
        if (original) constraintSet02.applyTo(constraintLayout)
        else constraintSet01.applyTo(constraintLayout)
        original = !original
        return true
      }
      else -> return super.onOptionsItemSelected(item)
    }

    // endregion
  }
}
