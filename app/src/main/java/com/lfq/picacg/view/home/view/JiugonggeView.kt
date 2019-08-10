package com.lfq.picacg.view.home.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lfq.picacg.R

class JiugonggeView(context: Context, attributes: AttributeSet) : LinearLayout(context, attributes) {

    private var images: Array<ImageView>
    var urls: List<String> = ArrayList()

    fun create() {
        for ((i, image) in images.withIndex()) {
            if (i < urls.size) {
                val options = RequestOptions()
                    .placeholder(R.drawable.ic_image)
                    .override(256)
                Glide.with(this)
                    .load(urls[i])
                    .apply(options)
                    .into(image)
                image.visibility = View.VISIBLE
            } else {
                image.visibility = View.GONE
            }
        }
        println(urls.size)
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_jiugongge, this)
        images = arrayOf(
            findViewById(R.id.image_1),
            findViewById(R.id.image_2),
            findViewById(R.id.image_3),
            findViewById(R.id.image_4),
            findViewById(R.id.image_5),
            findViewById(R.id.image_6),
            findViewById(R.id.image_7),
            findViewById(R.id.image_8),
            findViewById(R.id.image_9)
        )
        viewTreeObserver.addOnGlobalLayoutListener {
            //viewTreeObserver.removeGlobalOnLayoutListener()
            val size = width / 3
            for (image in images) {
                val params = LayoutParams(size - 1, size)
                if (image == images[1] || image == images[4] || image == images[7]) {
                    params.leftMargin = 1
                    params.rightMargin = 1
                }
                image.layoutParams = params
            }
            val layout = findViewById<LinearLayout>(R.id.layout)
            val params = layout.layoutParams as LayoutParams
            params.topMargin = 1
            params.bottomMargin = 1
            layout.layoutParams = params
        }
    }
}