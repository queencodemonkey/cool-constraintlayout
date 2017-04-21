package com.randomlytyping.ccl

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.transition.TransitionManager
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.randomlytyping.ccl.util.inflateInto
import com.randomlytyping.ccl.util.setUpAppBar
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * Examples of ConstraintSet including transitioning between sets.
 */
class ConstraintSetActivity : AppCompatActivity() {

  @BindView(R.id.constraint_layout) internal lateinit var constraintLayout: ConstraintLayout

  private var constraintSet01: ConstraintSet = ConstraintSet()
  private var constraintSet02: ConstraintSet = ConstraintSet()

  private var original: Boolean = true

  //region // Activity lifecycle

  override fun attachBaseContext(newBase: Context?) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_container_linear_layout)

    // Inflate content and bind views.
    ButterKnife.bind(this, inflateInto<ViewGroup>(R.id.linear_layout, R.layout.content_constraintset_01))

    setUpAppBar()

    // Load ConstraintSets.
    constraintSet01.clone(constraintLayout)
    constraintSet02.clone(this, R.layout.content_constraintset_02)
  }

  // endregion

  //region // Action item handling

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.constraint_set, menu)

    val menuItem:MenuItem = menu.findItem(R.id.action_swap_constraint_set)
    DrawableCompat.setTint(menuItem.icon, Color.WHITE)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.action_swap_constraint_set -> {
        TransitionManager.beginDelayedTransition(constraintLayout)
        if (original) constraintSet02.applyTo(constraintLayout)
        else constraintSet01.applyTo(constraintLayout)
        original = !original
        return true
      }
      else -> return super.onOptionsItemSelected(item)
    }
  }

  // endregion
}
