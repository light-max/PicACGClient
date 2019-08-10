package com.lfq.picacg.view.info

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.lfq.picacg.MyApplication.Companion.context
import com.lfq.picacg.R
import com.lfq.picacg.call.ListItemCall
import com.lfq.picacg.call.MyNetCall
import com.lfq.picacg.data.AuthorInfoRequest
import com.lfq.picacg.data.ContentRequest
import com.lfq.picacg.service.imp.*
import com.lfq.picacg.util.UserTools
import com.lfq.picacg.util.getDateTime
import com.lfq.picacg.util.toast
import com.lfq.picacg.view.ImageBrowse
import com.lfq.picacg.view.UpdateSubmission
import com.lfq.picacg.view.atlasdetails.AtlasDetailsActivity
import com.lfq.picacg.view.login.LoginActivity
import de.hdodenhof.circleimageview.CircleImageView
import me.imid.swipebacklayout.lib.SwipeBackLayout
import me.imid.swipebacklayout.lib.app.SwipeBackActivity

class AuthorInfoActivity : SwipeBackActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_ALL)
        setContentView(R.layout.activity_authorinfo)
        val id = intent.getIntExtra(AUTHOR_ID, 0)
        //是否是以登录用户的身份打开这个activity
        isuser = UserTools.userinfo?.id == id
        val swipe = findViewById(R.id.swipe) as SwipeRefreshLayout
        swipe.setOnRefreshListener {
            post_author(id)
            post_content(id, 0, true, Runnable {
                swipe.isRefreshing = false
            })
        }
        recycler = findViewById(R.id.recycler) as RecyclerView
        @Suppress("DEPRECATION")
        recycler.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (up && newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    val manager = recyclerView.layoutManager as LinearLayoutManager
                    val position = manager.findLastVisibleItemPosition()
                    if (position + 1 == adapter.itemCount) {
                        pullUp(id)
                    }
                }
            }

            var up = false
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                up = dy > 0
            }
        })
        recycler.adapter = adapter
        post_author(id)
        post_content(id, 0, true, null)
    }

    //是否是以登录用户的身份打开这个activity
    var isuser = false
    lateinit var recycler: RecyclerView

    private fun post_author(id: Int) {
        get_authorinfo(id, object : MyNetCall<AuthorInfoRequest> {
            override fun onRequest(t: AuthorInfoRequest?) {
                when (t?.code) {
                    0 -> {
                        adapter.author = t
//                        adapter.notifyItemChanged(0) 这里不刷新，等获取内容后再刷新
                    }
                    -1 -> toast("系统错误")
                }
            }
        })
    }

    private fun post_content(id: Int, lastid: Int, reset: Boolean, runnable: Runnable?) =
        get_content(id, lastid, object : MyNetCall<ContentRequest> {
            override fun onRequest(t: ContentRequest?) {
                when (t?.code) {
                    0 -> {
                        if (reset) {
                            adapter.list.clear()
                            adapter.list.addAll(t.content)
//                            adapter.notifyItemRangeChanged(1, adapter.list.size) 直接刷新全部了
                            adapter.notifyDataSetChanged()
                        } else {
                            val index = adapter.itemCount
                            adapter.list.addAll(t.content)
                            val size = adapter.itemCount - index
                            adapter.notifyItemRangeChanged(index, size)
                        }
                    }
                    else -> toast("系统错误")
                }
                runnable?.run()
            }
        })

    companion object {
        val AUTHOR_ID = "AUTHOR_ID"
    }

    private fun pullUp(id: Int) {
        if (adapter.pullup) return
        adapter.pullup = true
        post_content(id, adapter.getLastId(), false, Runnable {
            adapter.pullup = false
        })
    }

    private val adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return if (viewType == TYPE_HEAD) {
                HeadItem(LayoutInflater.from(parent.context).inflate(R.layout.item_authorinfo, parent, false))
            } else {
                ImageItem(LayoutInflater.from(parent.context).inflate(R.layout.item_atlas2, parent, false))
            }
        }

        override fun getItemCount(): Int {
            return list.size + 1
        }

        override fun getItemViewType(position: Int): Int {
            if (position == 0) {
                return TYPE_HEAD
            }
            return TYPE_IMAGE
        }

        val list = ArrayList<ContentRequest.ContentBean>()
        var author: AuthorInfoRequest? = null

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder.itemViewType == TYPE_IMAGE) {
                val bean = list[position - 1]
                holder as ImageItem
                holder.setBean(bean)
                holder.itemView.setOnClickListener {
                    itemClick.onImageClick(bean, position)
                }
                holder.star_bt.setOnClickListener {
                    starClick.onStarClick(bean, position)
                }
                if (isuser) {
                    holder.more.visibility = View.VISIBLE
                    holder.more.setOnClickListener {
                        moreClick.onMoreClick(holder.more, bean, position)
                    }
                } else {
                    holder.more.visibility = View.GONE
                }
            } else if (holder.itemViewType == TYPE_HEAD) {
                holder as HeadItem
                if (author != null) {
                    holder.setData(author!!)
                }
            }
        }

        inner class ImageItem(view: View) : RecyclerView.ViewHolder(view) {
            val icon = view.findViewById<ImageView>(R.id.icon)
            val title = view.findViewById<TextView>(R.id.title)
            val time = view.findViewById<TextView>(R.id.time)
            val number = view.findViewById<TextView>(R.id.number)
            val star = view.findViewById<TextView>(R.id.star)
            val watch = view.findViewById<TextView>(R.id.watch)
            val star_bt = view.findViewById<ImageView>(R.id.star_bt)
            val more = view.findViewById<ImageView>(R.id.more)

            fun setBean(bean: ContentRequest.ContentBean) {
                Glide.with(itemView)
                    .load(bean.thumbnails[0])
                    .into(icon)
                title.text = bean.title
                time.text = getDateTime(bean.releasetime)
                number.text = bean.number.toString()
                star.text = bean.star.toString()
                watch.text = bean.watch.toString()
            }
        }

        inner class HeadItem(view: View) : RecyclerView.ViewHolder(view) {
            val head = view.findViewById(R.id.head) as CircleImageView
            val nickname = view.findViewById(R.id.nickname) as TextView
            val sex = view.findViewById(R.id.sex) as TextView
            val number = view.findViewById(R.id.number) as TextView
            val word = view.findViewById(R.id.word) as TextView

            fun setData(data: AuthorInfoRequest) {
                val options = RequestOptions()
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_account_circle_black_24dp)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .override(256)
                Glide.with(itemView)
                    .load(data.img_small)
                    .apply(options)
                    .into(head)
                head.setOnClickListener {
                    val intent = Intent(this@AuthorInfoActivity, ImageBrowse::class.java)
                    intent.putExtra("url", data.img_source)
                    startActivity(intent)
                }
                nickname.text = data.nickname
                sex.text = data.sex
                number.text = data.number.toString()
                word.text = data.word
            }
        }

        var pullup = false
        val TYPE_HEAD = 1
        val TYPE_IMAGE = 2

        fun getLastId(): Int {
            if (list.size == 0) {
                return 0
            }
            return list.last().id
        }
    }

    private val itemClick = object : ListItemCall.OnImageClickListener {
        override fun onImageClick(bean: ContentRequest.ContentBean, position: Int) {
            watch_manuscript(bean.id, object : MyNetCall<Int> {
                override fun onRequest(t: Int?) {
                    if (t == 0) watch(recycler, position)
                }
            })
            val intent = Intent(this@AuthorInfoActivity, AtlasDetailsActivity::class.java)
            intent.putExtra(AtlasDetailsActivity.DATA, bean)
            startActivity(intent)
        }
    }

    private val starClick = object : ListItemCall.OnStarClickListener {
        override fun onStarClick(bean: ContentRequest.ContentBean, position: Int) {
            if (UserTools.key == 0L) {
                toast("登录后才能点赞")
                startActivity(Intent(this@AuthorInfoActivity, LoginActivity::class.java))
                return
            }
            star_manuscript(bean.id, object : MyNetCall<Int> {
                override fun onRequest(t: Int?) {
                    when (t) {
                        -1 -> toast("系统错误")
                        0 -> star(recycler, position)
                        1 -> toast("已经点过赞啦")
                    }
                }
            })
        }
    }

    private val moreClick = object : ListItemCall.OnMoreClickListener {
        override fun onMoreClick(parent: View, bean: ContentRequest.ContentBean, position: Int) {
            val view = View.inflate(context, R.layout.view_moremenu, null)
            val edit = view.findViewById<TextView>(R.id.edit)
            val delete = view.findViewById<TextView>(R.id.delete)
            val window = showPopupWindow(parent, view)
            edit.setOnClickListener {
                val intent = Intent(this@AuthorInfoActivity, UpdateSubmission::class.java)
                intent.putExtra(UpdateSubmission.DATA, bean)
                intent.putExtra("position", position)
                startActivityForResult(intent, 1)
                window.dismiss()
            }
            delete.setOnClickListener {
                showDeleteDialog(bean, position)
                window.dismiss()
            }
        }
    }

    private fun showDeleteDialog(bean: ContentRequest.ContentBean, position: Int) {
        AlertDialog.Builder(this@AuthorInfoActivity)
            .setTitle("你确定要删除这个稿件吗? 此操作无法逆转")
            .setNegativeButton("取消", null)
            .setPositiveButton("确定") { _, _ ->
                detele_submission(bean.id, object : MyNetCall<Int> {
                    override fun onRequest(t: Int?) {
                        if (t == 0) {
                            adapter.list.remove(bean)
                            adapter.notifyItemRemoved(position)//有删除的动画
                            adapter.notifyItemRangeRemoved(position, adapter.itemCount - position)//重新onBindViewHolder
                            toast("删除成功")
                        } else if (t == -1) toast("登录信息已失效，请刷新登录界面或者重新登录")
                    }
                })
            }.show()
    }

    private fun showPopupWindow(parent: View, view: View): PopupWindow {
        val window = PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        window.animationStyle = R.style.Transparent_Dialog
        window.setBackgroundDrawable(ColorDrawable())
        window.showAsDropDown(parent)
        return window
    }

    private fun watch(recycler: RecyclerView, position: Int) {
        val watch = ++adapter.list[position - 1].watch
        val holder = recycler.findViewHolderForAdapterPosition(position)
        val textView = holder?.itemView?.findViewById<TextView>(R.id.watch)
        textView?.text = watch.toString()
    }

    private fun star(recycler: RecyclerView, position: Int) {
        val star = ++adapter.list[position - 1].star
        val holder = recycler.findViewHolderForAdapterPosition(position)
        val textView = holder?.itemView?.findViewById<TextView>(R.id.star)
        textView?.text = star.toString()
    }

    private fun updateTitle(recycler: RecyclerView, position: Int, title: String) {
        val holder = recycler.findViewHolderForAdapterPosition(position)
        val textView = holder?.itemView?.findViewById<TextView>(R.id.title)
        textView?.text = title
    }

    /**
     * 接收上一个修改投稿信息activity返回的修改状态
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            val position = data.getIntExtra("position", 0)
            if (position == 0) {
                toast("出错了")
                return
            }
            val bean = adapter.list[position - 1]
            val title = data.getStringExtra("title")
            val keyword = data.getStringExtra("keyword")
            val introduction = data.getStringExtra("introduction")
            bean.title = title
            bean.keyword = keyword
            bean.introduction = introduction
            updateTitle(findViewById(R.id.recycler) as RecyclerView, position, title)
        }
    }
}