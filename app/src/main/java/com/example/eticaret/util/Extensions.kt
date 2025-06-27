package com.example.eticaret.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
 
fun ImageView.loadUrl(url: String) {
    Glide.with(this.context)
        .load(url)
        .apply(RequestOptions()
            .centerInside()
            .dontTransform())
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

fun ImageView.loadUrlWithFitCenter(url: String) {
    Glide.with(this.context)
        .load(url)
        .apply(RequestOptions()
            .fitCenter()
            .dontTransform())
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
} 