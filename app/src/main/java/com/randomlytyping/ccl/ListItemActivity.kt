package com.randomlytyping.ccl

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.bindView
import com.randomlytyping.ccl.util.*
import rt.randamu.getResourceIdArray

/**
 * Example of using [ConstraintLayout] as roots for list items.
 */
class ListItemActivity : AppCompatActivity() {

  //region // Fields
  private val recyclerView by bindView<RecyclerView>(R.id.recycler_view)

  //endregion

  //region // Activity lifecycle

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_container_linear_layout)

    // Inflate content and bind views.
    inflateInto<ViewGroup>(R.id.linear_layout, R.layout.content_recycler_view)

    setUpAppBar()

    recyclerView.layoutManager = LinearLayoutManager(this)
    recyclerView.adapter = UnsplashAdapter(this)
  }

  //endregion

  //region // RecyclerView/List

  /**
   * RecyclerView adapter
   *
   * @property context The current context.
   * @property inflater Layout inflater.
   * @property images List of drawable resource IDs for displayed photos.
   * @property attributions List of string resource IDs for image attributions.
   */
  private class UnsplashAdapter(private val context: Context,
                                private val inflater: LayoutInflater = LayoutInflater.from(context),
                                private val images: List<Int> = context.resources.getResourceIdArray(R.array.unsplash_images),
                                private val attributions: List<Int> = context.resources.getResourceIdArray(R.array.unsplash_attributions))
    : RecyclerView.Adapter<UnsplashViewHolder>() {

    /**
     * List of string resource IDs for image URLs.
     */
    private val urls: List<Int> = context.resources.getResourceIdArray(R.array.unsplash_urls)


    //
    // RecyclerView.Adapter<UnsplashViewHolder> implementation
    //

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemCount() = images.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        UnsplashViewHolder(inflater.inflate(R.layout.list_item_unsplash, parent, false),
                           R.layout.list_item_unsplash_expanded)

    override fun onBindViewHolder(holder: UnsplashViewHolder, position: Int) {
      holder.imageDrawable = ContextCompat.getDrawable(context, images[position])
      holder.attributionResId = attributions[position]
      holder.urlResId = urls[position]
    }
  }

  /**
   * View holder
   */
  private class UnsplashViewHolder(itemView: View,
                                   @LayoutRes alternateLayout: Int,
                                   private val constraintLayout: ConstraintLayout = itemView as ConstraintLayout,
                                   private val attributionView: TextView = itemView.findById(R.id.attribution),
                                   private val urlView: TextView = itemView.findById(R.id.url),
                                   private val imageView: ImageView = itemView.findById(R.id.image),
                                   private var primary: Boolean = true)
    : RecyclerView.ViewHolder(itemView) {


    private val constraintSet01: ConstraintSet = constraintLayout.toConstraintSet()
    private val constraintSet02: ConstraintSet = ConstraintSets.from(itemView.context, alternateLayout)

    /**
     * ID of attribution string resource.
     */
    var attributionResId = 0
      set(value) {
        attributionView.setText(value)
      }

    /**
     * ID of url string resource.
     */
    var urlResId = 0
      set(value) {
        urlView.setText(value)
      }

    /**
     * Drawable resource.
     */
    var imageDrawable: Drawable = ColorDrawable(Color.TRANSPARENT)
      set(value) {
        imageView.setImageDrawable(value)
      }

    init {
      itemView.setOnClickListener { swap() }
    }

    //region // Constraint set switching

    /**
     * Swap constraint sets.
     */
    fun swap() {
      TransitionManager.beginDelayedTransition(constraintLayout)
      if (primary) constraintSet02.applyTo(constraintLayout)
      else constraintSet01.applyTo(constraintLayout)
      primary = !primary
    }
  }

  //endregion
}
