package com.randomlytyping.ccl

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.ButterKnife
import rt.randamu.getResourceIdArray

/**
 * Launcher activity + example selector.
 */
class MainActivity : AppCompatActivity() {

  //region // Activity lifecycle

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    ButterKnife.findById<RecyclerView>(this, android.R.id.list).also {
      it.adapter = ExampleListAdapter()
      it.layoutManager = LinearLayoutManager(this)
    }
  }

  //endregion

  //region // Example navigation

  fun navigateToExample(@StringRes exampleResId: Int) {
    startActivity(Intent(this, when (exampleResId) {
      R.string.example_alignment -> AlignmentActivity::class.java
      R.string.example_dimension_ratio -> DimensionRatioActivity::class.java
      R.string.example_chains -> ChainsActivity::class.java
      R.string.example_dynamic_constraint_layout -> DynamicConstraintLayoutActivity::class.java
    //        R.string.example_constraint_set -> ConstraintSetActivity::class.java
      else -> throw IllegalArgumentException("Invalid item: $exampleResId")
    }))
  }

  //endregion

  //region // Example RecyclerView/List

  /**
   * RecyclerView adapter
   */
  private inner class ExampleListAdapter(val inflater: LayoutInflater = LayoutInflater.from(this))
    : RecyclerView.Adapter<ExampleViewHolder>() {

    /**
     * List of string resource IDs that represent all the navigable examples.
     */
    private val examples: List<Int> = resources.getResourceIdArray(R.array.examples)

    /**
     * List of icon resource IDs that represent all the navigable examples.
     */
    private val icons: List<Int> = resources.getResourceIdArray(R.array.example_icons)

    /**
     * Color used to tint icons.
     */
    @ColorInt private val tint:Int = ContextCompat.getColor(this@MainActivity, R.color.icon_active_color)

    //
    // RecyclerView.Adapter<ExampleViewHolder> implementation
    //

    override fun getItemCount() = examples.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ExampleViewHolder(
            inflater.inflate(R.layout.list_item_example, parent, false),
            this@MainActivity::navigateToExample,
            tint
        )

    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
      holder.resId = examples[position]
      holder.iconId = icons[position]
    }
  }

  //endregion

  //region // Inner classes

  /**
   * View holder
   */
  private class ExampleViewHolder(itemView: View,
                                  exampleListener: (Int) -> Unit,
                                  @ColorInt private val tint:Int,
                                  private val textView: TextView = itemView as TextView)
    : RecyclerView.ViewHolder(itemView) {

    /**
     * ID of string resource for example title.
     */
    var resId = 0
      set(value) {
        field = value
        textView.setText(value)
      }

    /**
     * ID of drawable resource for example icon.
     */
    var iconId = 0
      set(value) {
        field = value
        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(value, 0, 0, 0)
        DrawableCompat.setTint(textView.compoundDrawablesRelative[0], tint)
      }

    init {
      itemView.setOnClickListener { exampleListener(resId) }
    }
  }

  //endregion
}