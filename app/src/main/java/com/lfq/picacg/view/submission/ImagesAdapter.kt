package com.lfq.picacg.view.submission

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lfq.picacg.R
import java.io.File

class ImagesAdapter(val addlistener: View.OnClickListener) : RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {
    var list = ArrayList<File>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(View.inflate(parent.context, R.layout.item_image, null))
    }

    override fun getItemCount(): Int {
        return list.size + 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == list.size) {
            holder.image.setImageResource(R.drawable.ic_add)
            holder.image.setOnClickListener(addlistener)
        } else {
            Glide.with(holder.itemView)
                .load(list[position])
                .into(holder.image)
            holder.image.setOnClickListener {
                AlertDialog.Builder(holder.image.context)
                    .setMessage("是否移除这张图片?")
                    .setNegativeButton("否", null)
                    .setPositiveButton("是") { _, _ ->
                        list.removeAt(position)
                        notifyDataSetChanged()
                    }.show()
            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.image)
    }
}