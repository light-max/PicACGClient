package com.lfq.picacg.view.home.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.lfq.picacg.R
import com.lfq.picacg.call.ListItemCall
import com.lfq.picacg.call.MyNetCall
import com.lfq.picacg.data.ContentRequest
import com.lfq.picacg.service.imp.get_content
import com.lfq.picacg.service.imp.rand_content
import com.lfq.picacg.service.imp.star_manuscript
import com.lfq.picacg.service.imp.watch_manuscript
import com.lfq.picacg.util.UserTools
import com.lfq.picacg.util.toast
import com.lfq.picacg.view.atlasdetails.AtlasDetailsActivity
import com.lfq.picacg.view.home.adapter.AtlasListAdapter
import com.lfq.picacg.view.info.AuthorInfoActivity
import com.lfq.picacg.view.login.LoginActivity

class HomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home, container, false)
    }

    private val adapter = AtlasListAdapter()
    private lateinit var recycler: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler = view.findViewById(R.id.recycler)
        recycler.adapter = adapter
        val swipe = view.findViewById<SwipeRefreshLayout>(R.id.swipe)
        swipe.setOnRefreshListener {
            rand_content(object : MyNetCall<ContentRequest> {
                override fun onRequest(t: ContentRequest?) {
                    if (t != null) {
                        adapter.clear()
                        adapter.addAll(t.content)
                        adapter.notifyDataSetChanged()
                    }
                    swipe.isRefreshing = false
                }
            })
        }
        @Suppress("DEPRECATION")
        recycler.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (up && newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    val manager = recyclerView.layoutManager as LinearLayoutManager
                    val position = manager.findLastVisibleItemPosition()
                    if (position + 1 == adapter.itemCount) {
                        pullUp()
                    }
                }
            }

            var up = false
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                up = dy > 0
            }
        })
        adapter.imageClickListener = imageClick
        adapter.headClickListener = headClick
        adapter.starClickListener = starClick
        post()
    }

    private fun post() = rand_content(object : MyNetCall<ContentRequest> {
        override fun onRequest(t: ContentRequest?) {
            if (t == null) return
            adapter.addAll(t.content)
            adapter.notifyDataSetChanged()
        }
    })

    private fun pullUp() {
        if (adapter.pullup) return
        adapter.pullup = true
        get_content(adapter.getIdToJsonArray(), object : MyNetCall<ContentRequest> {
            override fun onRequest(t: ContentRequest?) {
                adapter.pullup = false
                if (t == null) return
                val index = adapter.itemCount
                if (adapter.addAll(t.content) == 0) {
//                    toast("没有更多内容了")
                } else {
                    adapter.notifyItemRangeChanged(index, adapter.itemCount - index + 1)
                }
            }
        })
    }

    private val imageClick = object : ListItemCall.OnImageClickListener {
        override fun onImageClick(bean: ContentRequest.ContentBean, position: Int) {
            watch_manuscript(bean.id, object : MyNetCall<Int> {
                override fun onRequest(t: Int?) {
                    if (t == 0) adapter.watch(recycler, position)
                }
            })
            val intent = Intent(context, AtlasDetailsActivity::class.java)
            intent.putExtra(AtlasDetailsActivity.DATA, bean)
            context?.startActivity(intent)
        }
    }

    private val headClick = object : ListItemCall.OnHeadClickListener {
        override fun onHeadClick(bean: ContentRequest.ContentBean) {
            val intent = Intent(context, AuthorInfoActivity::class.java)
            intent.putExtra(AuthorInfoActivity.AUTHOR_ID, bean.author)
            context?.startActivity(intent)
        }
    }

    private val starClick = object : ListItemCall.OnStarClickListener {
        override fun onStarClick(bean: ContentRequest.ContentBean, position: Int) {
            if (UserTools.key == 0L) {
                toast("登录后才能点赞")
                context?.startActivity(Intent(context, LoginActivity::class.java))
                return
            }
            star_manuscript(bean.id, object : MyNetCall<Int> {
                override fun onRequest(t: Int?) {
                    when (t) {
                        -1 -> toast("系统错误")
                        0 -> adapter.star(recycler, position)
                        1 -> toast("已经点过赞啦")
                    }
                }
            })
        }
    }
}