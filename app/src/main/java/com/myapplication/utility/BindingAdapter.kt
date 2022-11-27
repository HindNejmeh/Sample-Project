package com.myapplication.utility

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.textfield.TextInputLayout
import com.myapplication.R

@BindingAdapter("required")
fun setInputRequired(textInputLayout: TextInputLayout, isRequired: Boolean) {
    textInputLayout.apply {
        hint?.let {
            if (isRequired && !it.endsWith(" *")) {
                hint = "$it *"
            } else if (!isRequired && it.endsWith(" *")) {
                hint = it.dropLast(2)
            }
        }
    }
}

@BindingAdapter("clearErrorAfterTextChanged")
fun setClearErrorAfterTextChanged(textInputLayout: TextInputLayout, isEnabled: Boolean) {
    if (isEnabled) {
        textInputLayout.editText?.doAfterTextChanged { textInputLayout.error = null }
    }
}

@BindingAdapter("visibleIf")
fun setVisible(view: View, isVisible: Boolean) {
    view.isVisible = isVisible
}

private val shimmer = Shimmer.AlphaHighlightBuilder()
    .setDirection(Shimmer.Direction.TOP_TO_BOTTOM) // default is left to right
    .setDropoff(1F) // default is 0.5
    .setDuration(3000) // default is 1 second
    .setTilt(0F) // default is 20 degrees
    .build()

@BindingAdapter("shimmerIf")
fun setShimmering(shimmerFrameLayout: ShimmerFrameLayout, isShimmering: Boolean) {
    shimmerFrameLayout.apply {
        setShimmer(shimmer)

        isVisible = isShimmering

        if (isShimmering) startShimmer() else stopShimmer()
    }
}

@SuppressLint("ResourceType")
@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, imageUrl: String?) {
    val shimmerDrawable = ShimmerDrawable().apply { setShimmer(shimmer) }

    // shimmering
    imageView.setBackgroundColor(
        ContextCompat.getColor(
            imageView.context, R.color.colorShimmer
        )
    )

    val drawableCrossFadeFactory =
        DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(false).build()
    val requestListener = object : RequestListener<Drawable> {

        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean,
        ) = run {
            false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean,
        ) = run {
            imageView.setBackgroundColor(
                ContextCompat.getColor(
                    imageView.context, R.color.white
                )
            )

            false
        }
    }

    Glide.with(imageView).load(imageUrl)
        .transition(DrawableTransitionOptions.withCrossFade(drawableCrossFadeFactory))
        .placeholder(shimmerDrawable).error(R.drawable.ic_error).addListener(requestListener)
        .into(imageView)
}

@BindingAdapter("adapter")
fun setAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    recyclerView.adapter = adapter
}


