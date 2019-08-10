package com.lfq.picacg.view.home.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.lfq.picacg.MyApplication
import com.lfq.picacg.R
import com.lfq.picacg.call.ListItemCall
import com.lfq.picacg.data.ContentRequest
import com.lfq.picacg.util.getDateTime
import com.lfq.picacg.util.head_small_url
import com.lfq.picacg.view.home.view.JiugonggeView
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONArray

/**
 * 图集列表适配器
 */
class AtlasListAdapter : RecyclerView.Adapter<AtlasListAdapter.ViewHolder>() {
    private val list = ArrayList<ContentRequest.ContentBean>()

    var pullup = false
    var imageClickListener: ListItemCall.OnImageClickListener? = null
    var headClickListener: ListItemCall.OnHeadClickListener? = null
    var starClickListener: ListItemCall.OnStarClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(View.inflate(parent.context, R.layout.item_atlas, null))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bean = list[position]
        val options = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
//            .skipMemoryCache(true) //内存缓存 磁盘不缓存
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_account_circle_black_24dp)
            .override(128)
        Glide.with(MyApplication.context)
            .load(head_small_url + bean.author)
            .apply(options)
            .into(holder.head)
        holder.title.text = bean.title
        holder.nickname.text = bean.authorname
        holder.keyword.text = bean.keyword
        holder.images.urls = bean.thumbnails
        holder.images.create()
        holder.number.text = bean.number.toString()
        holder.star.text = bean.star.toString()
        holder.watch.text = bean.watch.toString()
        holder.releasetime.text = getDateTime(bean.releasetime)
        holder.images.setOnClickListener {
            imageClickListener?.onImageClick(bean, position)
        }
        holder.head.setOnClickListener {
            headClickListener?.onHeadClick(bean)
        }
        holder.star_bt.setOnClickListener {
            starClickListener?.onStarClick(bean, position)
        }
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val head: CircleImageView = view.findViewById(R.id.head)
        val nickname: TextView = view.findViewById(R.id.nickname)
        val title: TextView = view.findViewById(R.id.title)
        val keyword: TextView = view.findViewById(R.id.keyword)
        val images: JiugonggeView = view.findViewById(R.id.images)
        val number: TextView = view.findViewById(R.id.number)
        val star: TextView = view.findViewById(R.id.star)
        val watch: TextView = view.findViewById(R.id.watch)
        val star_bt: ImageView = view.findViewById(R.id.star_bt)
        val releasetime: TextView = view.findViewById(R.id.releasetime)
    }

    fun star(view: RecyclerView, position: Int) {
        val star = ++list[position].star
        val holder = view.findViewHolderForAdapterPosition(position) as ViewHolder
        holder.star.text = star.toString()
    }

    fun watch(view: RecyclerView, position: Int) {
        val watch = ++list[position].watch
        val holder = view.findViewHolderForAdapterPosition(position) as ViewHolder
        holder.watch.text = watch.toString()
    }

    fun add(data: ContentRequest.ContentBean) {
        list.remove(data)
        list.add(data)
    }

    fun addAll(datas: Collection<ContentRequest.ContentBean>): Int {
        val size = list.size
        for (data in datas) {
            if (list.indexOf(data) == -1) {
                list.add(data)
            }
        }
        return list.size - size
    }

    fun clear() {
        list.clear()
    }

    fun getIdToJsonArray(): JSONArray {
        val array = JSONArray()
        list.forEach { content ->
            array.put(content.id)
        }
        return array
    }
}