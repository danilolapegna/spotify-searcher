package com.spotifysearch.util

import android.content.Context
import android.widget.ImageView
import com.spotifysearch.R
import com.spotifysearch.rest.model.RealmImage
import com.squareup.picasso.Picasso

object ImageUtil {

    fun loadImageInCollapsingToolbar(context: Context?, picasso: Picasso, image: RealmImage?, imageView: ImageView?) {
        if (context != null) {
            image?.let {
                picasso.load(image.url)
                        .fit()
                        .centerInside()
                        .placeholder(R.drawable.ic_camera_placeholder)
                        .error(R.drawable.ic_camera_placeholder)
                        .noFade()
                        .into(imageView)
            }
        }
    }
}