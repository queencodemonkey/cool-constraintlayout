package com.randomlytyping.ccl

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.randomlytyping.ccl.util.inflateInto
import com.randomlytyping.ccl.util.setUpAppBar
import rt.randamu.getResourceIdArray

class ConstraintLayoutListItemActivity : AppCompatActivity() {

  //region // Fields

  @BindView(R.id.recycler_view) lateinit var recyclerView: RecyclerView

  //endregion

  //region // Activity lifecycle

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_container_linear_layout)

    // Inflate content and bind views.
    ButterKnife.bind(this, inflateInto<ViewGroup>(R.id.linear_layout, R.layout.content_recycler_view))

    setUpAppBar()

    recyclerView.layoutManager = LinearLayoutManager(this)
    recyclerView.adapter = UnsplashAdapter(this)
  }

  //endregion

  //region // RecyclerView/List

  /**
   * RecyclerView adapter
   */
  private class UnsplashAdapter(private val context: Context,
                                private val inflater: LayoutInflater = LayoutInflater.from(context))
    : RecyclerView.Adapter<UnsplashViewHolder>() {

    /**
     * List of drawable resource IDs for displayed photos.
     */
    private val images: List<Int> = context.resources.getResourceIdArray(R.array.unsplash_images)

    /**
     * List of string resource IDs for image attributions.
     */
    private val attributions: List<Int> = context.resources.getResourceIdArray(R.array.unsplash_attributions)

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
            ConstraintSet().apply { clone(context, R.layout.list_item_unsplash_expanded) })

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
                                   private val constraintSet02: ConstraintSet,
                                   private val constraintLayout: ConstraintLayout = itemView as ConstraintLayout,
                                   private val attributionView: TextView = ButterKnife.findById(itemView, R.id.attribution),
                                   private val urlView: TextView = ButterKnife.findById(itemView, R.id.url),
                                   private val imageView: ImageView = ButterKnife.findById(itemView, R.id.image),
                                   private val constraintSet01: ConstraintSet = ConstraintSet().apply { clone(constraintLayout) },
                                   private var primary: Boolean = true)
    : RecyclerView.ViewHolder(itemView) {

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
