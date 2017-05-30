package com.randomlytyping.ccl

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintLayout.LayoutParams.*
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import butterknife.bindView
import com.randomlytyping.ccl.util.*

class ChainsActivity : AppCompatActivity() {

  companion object {
    private val CHAIN_STYLE = listOf(CHAIN_SPREAD, CHAIN_SPREAD_INSIDE, CHAIN_PACKED)
  }

  private val constraintLayout by bindView<ConstraintLayout>(R.id.constraint_layout)
  private val constraintSet by lazy { constraintLayout.toConstraintSet() }

  private var indexVertical = 0
  private var indexD = 0

  //region // Activity lifecycle

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_container_linear_layout)

    // Inflate content and bind views.
    inflateInto<ViewGroup>(R.id.linear_layout, R.layout.content_chains)

    setUpAppBar()

    findById<View>(R.id.toggle_vertical).setOnClickListener {
      indexVertical = (indexVertical + 1) % CHAIN_STYLE.size
      constraintSet.update(constraintLayout) {
        setVerticalChainStyle(R.id.text_01, CHAIN_STYLE[indexVertical])
      }
    }

    findById<View>(R.id.toggle_d).setOnClickListener {
      indexD = (indexD + 1) % CHAIN_STYLE.size
      constraintSet.update(constraintLayout) {
        setHorizontalChainStyle(R.id.text_04, indexD)
      }
    }
  }

  //endregion
}

//endregion
