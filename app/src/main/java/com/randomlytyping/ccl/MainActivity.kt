package com.randomlytyping.ccl

import android.content.Intent
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.randomlytyping.ccl.util.findById
import rt.randamu.getResourceIdArray
import kotlin.properties.Delegates

/**
 * Launcher activity + example selector.
 */
class MainActivity : AppCompatActivity() {

  //region // Activity lifecycle

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    with(findById<RecyclerView>(android.R.id.list)) {
      adapter = ExampleListAdapter()
      layoutManager = LinearLayoutManager(this@MainActivity)
    }
  }

  //endregion

  //region // Example navigation

  fun navigateToExample(@StringRes exampleResId: Int) {
    startActivity(Intent(this, when (exampleResId) {
      R.string.example_alignment -> AlignmentActivity::class.java
      R.string.example_chains -> ChainsActivity::class.java
      R.string.example_list_item -> ListItemActivity::class.java
      R.string.example_constraint_set -> ConstraintSetActivity::class.java
      R.string.example_dimension_ratio -> DimensionRatioActivity::class.java
      R.string.example_dynamic_constraint_layout -> DynamicConstraintLayoutActivity::class.java
      else -> throw IllegalArgumentException("Invalid item: $exampleResId")
    }))
  }

  //endregion

  //region // Example RecyclerView/List

  /**
   * RecyclerView adapter
   *
   * @property examples List of string resource IDs that represent all the navigable examples.
   * @property icons List of icon resource IDs that represent all the navigable examples.
   * @property tint Color used to tint icons.
   */
  private inner class ExampleListAdapter(
      private val examples: List<Int> = resources.getResourceIdArray(R.array.examples),
      private val icons: List<Int> = resources.getResourceIdArray(R.array.example_icons),
      @ColorInt private val tint: Int = ContextCompat.getColor(this@MainActivity, R.color.icon_active_color))
    : RecyclerView.Adapter<ExampleViewHolder>() {

    //
    // RecyclerView.Adapter<ExampleViewHolder> implementation
    //

    override fun getItemCount() = examples.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ExampleViewHolder(
            layoutInflater.inflate(R.layout.list_item_example, parent, false),
            this@MainActivity::navigateToExample,
            tint)

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
                                  @ColorInt private val tint: Int,
                                  private val textView: TextView = itemView as TextView)
    : RecyclerView.ViewHolder(itemView) {

    /**
     * ID of string resource for example title.
     */
    var resId by Delegates.observable(0) { _, _, new ->
      textView.setText(new)
    }

    /**
     * ID of drawable resource for example icon.
     */
    var iconId by Delegates.observable(0) { _, _, new ->
      textView.setCompoundDrawablesRelativeWithIntrinsicBounds(new, 0, 0, 0)
      DrawableCompat.setTint(textView.compoundDrawablesRelative[0], tint)
    }

    init {
      itemView.setOnClickListener { exampleListener(resId) }
    }
  }

  //endregion
}