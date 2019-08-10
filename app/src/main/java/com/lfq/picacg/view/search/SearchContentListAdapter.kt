package com.lfq.picacg.view.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lfq.picacg.R
import com.lfq.picacg.call.ListItemCall
import com.lfq.picacg.data.SearchRequest

class SearchContentListAdapter : RecyclerView.Adapter<SearchContentListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_searchcontent, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    val list = ArrayList<SearchRequest.SearchBean>()
    var listener: ListItemCall.OnSearchResClickListener? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bean = list[position]
        holder.text.text = bean.value
        Glide.with(holder.itemView)
            .load(bean.icon)
            .error(R.drawable.ic_image)
            .into(holder.icon)
        holder.itemView.setOnClickListener {
            listener?.onnResClick(bean)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.icon)
        val text: TextView = view.findViewById(R.id.text)
    }
}