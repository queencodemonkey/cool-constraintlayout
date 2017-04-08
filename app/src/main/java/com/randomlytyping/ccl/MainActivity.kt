package com.randomlytyping.ccl

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.ButterKnife

/**
 * Launcher activity + example selector.
 */
class MainActivity : AppCompatActivity() {

  //region // Activity lifecycle

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    ButterKnife.findById<RecyclerView>(this, android.R.id.list).also {
      it.adapter = ExampleListAdapter(this)
      it.layoutManager = LinearLayoutManager(this)
    }
  }

  //endregion

  //region // Example navigation

  fun navigateToExample(@StringRes exampleResId: Int) {
    startActivity(Intent(this, when (exampleResId) {
      R.string.example_alignment -> AlignmentActivity::class.java
      R.string.example_dimension_ratio -> DimensionRatioActivity::class.java
    //        R.string.example_chains -> ChainsActivity::class.java
    //        R.string.example_constraint_set -> ConstraintSetActivity::class.java
      else -> throw IllegalArgumentException("Invalid item: $exampleResId")
    }))
  }

  //endregion

  //region // Example RecyclerView/List

  /**
   * RecyclerView adapter
   */
  private inner class ExampleListAdapter(val context: Context,
                           val inflater: LayoutInflater = LayoutInflater.from(context))
    : RecyclerView.Adapter<ExampleViewHolder>() {

    /**
     * List of string resource IDs that represent all the navigable examples.
     */
    var examples: List<Int> = listOf()

    init {
      val typedArray = context.resources.obtainTypedArray(R.array.examples)
      examples = (0..typedArray.length() - 1).map { i -> typedArray.getResourceId(i, -1) }
      typedArray.recycle()
    }

    //
    // RecyclerView.Adapter<ExampleViewHolder> implementation
    //

    override fun getItemCount() = examples.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ExampleViewHolder(
            inflater.inflate(R.layout.list_item_example, parent, false),
            this@MainActivity::navigateToExample
        )

    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
      holder.resId = examples[position]
    }
  }

  //endregion

  //region // Inner classes

  /**
   * View holder
   */
  private class ExampleViewHolder(itemView: View,
                                  exampleListener: (Int) -> Unit,
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

    init {
      itemView.setOnClickListener { exampleListener(resId) }
    }
  }

  //endregion
}