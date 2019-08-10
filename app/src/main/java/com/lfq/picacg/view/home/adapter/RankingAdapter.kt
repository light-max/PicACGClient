package com.lfq.picacg.view.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lfq.picacg.R
import com.lfq.picacg.call.ListItemCall
import com.lfq.picacg.data.ContentRequest
import com.lfq.picacg.util.getDateTime
import com.lfq.picacg.util.toast

/**
 * 排行列表适配器
 */
class RankingAdapter : RecyclerView.Adapter<RankingAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_atlas2, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    var pullup = false
    val list: ArrayList<ContentRequest.ContentBean> = ArrayList()
    var itemClick: ListItemCall.OnImageClickListener? = null
    var starClick: ListItemCall.OnStarClickListener? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bean = list[position]
        Glide.with(holder.view)
            .load(bean.thumbnails[0])
            .into(holder.icon)
        holder.title.text = bean.title
        holder.time.text = getDateTime(bean.releasetime)
        holder.number.text = bean.number.toString()
        holder.star.text = bean.star.toString()
        holder.watch.text = bean.watch.toString()
        holder.star_bt.setOnClickListener {
            toast("star")
        }
        holder.itemView.setOnClickListener {
            itemClick?.onImageClick(bean, position)
        }
        holder.star_bt.setOnClickListener {
            starClick?.onStarClick(bean, position)
        }
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val icon = view.findViewById<ImageView>(R.id.icon)
        val title = view.findViewById<TextView>(R.id.title)
        val time = view.findViewById<TextView>(R.id.time)
        val number = view.findViewById<TextView>(R.id.number)
        val star = view.findViewById<TextView>(R.id.star)
        val watch = view.findViewById<TextView>(R.id.watch)
        val star_bt = view.findViewById<ImageView>(R.id.star_bt)
    }

    fun watch(view: RecyclerView, position: Int) {
        val watch = ++list[position].watch
        val holder = view.findViewHolderForAdapterPosition(position) as ViewHolder
        holder.watch.text = watch.toString()
    }

    fun star(view: RecyclerView, position: Int) {
        val star = ++list[position].star
        val holder = view.findViewHolderForAdapterPosition(position) as ViewHolder
        holder.star.text = star.toString()
    }
}