package com.randomlytyping.ccl

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.randomlytyping.ccl.util.inflateInto

class DimensionRatioActivity : AppCompatActivity() {

  //region // Fields

  @BindView(R.id.constraint_layout) lateinit var constraintLayout: ConstraintLayout

  //endregion

  //region // Activity lifecycle

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_container_linear_layout)

    // Inflate content and bind views.
    ButterKnife.bind(this, inflateInto<ViewGroup>(R.id.linear_layout, R.layout.content_dimension_ratio))

    // Set up app bar.
    setSupportActionBar(ButterKnife.findById<Toolbar>(this, R.id.app_bar))
    supportActionBar?.also {
      it.setDisplayShowHomeEnabled(true)
      it.setDisplayHomeAsUpEnabled(true)
    }
  }

  //endregion
}
