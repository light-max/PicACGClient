package com.lfq.picacg.view.atlasdetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lfq.picacg.R

/**
 * 图片列表适配器
 */
class ImageListAdapter(var context: Context, var imageClickListener: OnImageClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEAD) {
            HeadHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_imagelisthead, parent, false))
        } else {
            ImageHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image2, parent, false))
        }
    }

    val list = ArrayList<String>()
    var title = ""
    var introduction = ""

    override fun getItemCount(): Int {
        return list.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_HEAD
        } else {
            VIEW_TYPE_IMAGE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == VIEW_TYPE_HEAD) {
            holder as HeadHolder
            holder.title.text = title
            holder.introduction.text = introduction
        } else if (holder.itemViewType == VIEW_TYPE_IMAGE) {
            holder as ImageHolder
            val options = RequestOptions()
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_image)
            Glide.with(context)
                .load(list[position - 1])
                .apply(options)
                .into(holder.image)
            //查看照片
            holder.image.setOnClickListener {
                imageClickListener.onClick(position - 1)//要减去一个头部
            }
        }
    }

    private val VIEW_TYPE_HEAD = 1
    private val VIEW_TYPE_IMAGE = 2

    inner class ImageHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image = view.findViewById<ImageView>(R.id.image)
    }

    inner class HeadHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.title)
        val introduction = view.findViewById<TextView>(R.id.introduction)
    }

    /**
     * 点击某一张图片的回调
     */
    interface OnImageClickListener {
        fun onClick(position: Int)
    }
}
