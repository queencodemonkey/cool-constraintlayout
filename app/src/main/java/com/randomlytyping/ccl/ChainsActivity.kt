package com.randomlytyping.ccl

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintLayout.LayoutParams.*
import android.support.constraint.ConstraintSet
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.randomlytyping.ccl.util.inflateInto
import com.randomlytyping.ccl.util.setUpAppBar

class ChainsActivity : AppCompatActivity() {

  companion object {
    var CHAIN_STYLE = listOf(CHAIN_SPREAD, CHAIN_SPREAD_INSIDE, CHAIN_PACKED)
  }

  @BindView(R.id.constraint_layout) internal lateinit var constraintLayout: ConstraintLayout

  private var indexVertical = 0
  private var indexD = 0

  private val constraintSet = ConstraintSet()

  //region // Activity lifecycle

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_container_linear_layout)

    // Inflate content and bind views.
    ButterKnife.bind(this, inflateInto<ViewGroup>(R.id.linear_layout, R.layout.content_chains))

    setUpAppBar()

    // Initialize constraint set.
    constraintSet.clone(ButterKnife.findById<ConstraintLayout>(this, R.id.constraint_layout))
  }

  //endregion

  //region // Listeners

  @OnClick(R.id.toggle_vertical, R.id.toggle_d)
  internal fun onToggleChain(view: View) {
    when (view.id) {
      R.id.toggle_vertical -> {
        indexVertical = incrementIndex(indexVertical)
        constraintSet.setVerticalChainStyle(R.id.text_01, CHAIN_STYLE[indexVertical])
      }
      R.id.toggle_d -> {
        indexD = incrementIndex(indexD)
        constraintSet.setHorizontalChainStyle(R.id.text_04, indexD)
      }
      else -> throw IllegalArgumentException("Unhandled chain head ID: $view.id")
    }

    constraintSet.applyTo(constraintLayout)
  }

  //endregion

  //region // Helper extensions

  private fun incrementIndex(index: Int) = (index + 1) % CHAIN_STYLE.size

  //endregion
}
