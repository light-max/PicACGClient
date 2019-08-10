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
import com.lfq.picacg.service.imp.get_sort
import com.lfq.picacg.service.imp.star_manuscript
import com.lfq.picacg.service.imp.watch_manuscript
import com.lfq.picacg.util.UserTools
import com.lfq.picacg.util.toast
import com.lfq.picacg.view.atlasdetails.AtlasDetailsActivity
import com.lfq.picacg.view.home.adapter.RankingAdapter
import com.lfq.picacg.view.login.LoginActivity

class RankingChildFragment(private val type: String) : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.ranking_child, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val swipe = view.findViewById<SwipeRefreshLayout>(R.id.swipe)
        recycler = view.findViewById<RecyclerView>(R.id.recycler)
        recycler.adapter = adapter
        swipe.setOnRefreshListener {
            post(0, true, Runnable {
                swipe.isRefreshing = false
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
        adapter.itemClick = itemClick
        adapter.starClick = starClick
        post(getLastId(), false, null)
    }

    private val adapter = RankingAdapter()
    private lateinit var recycler: RecyclerView

    private fun post(lastid: Int, reset: Boolean, runnable: Runnable?) =
        get_sort(type, lastid, object : MyNetCall<ContentRequest> {
            override fun onRequest(t: ContentRequest?) {
                if (t?.code == 0) {
                    if (t.content.size == 0) {
//                        toast("没有更多内容了")
                    } else {
                        if (reset) {
                            adapter.list.clear()
                            adapter.list.addAll(t.content)
                            adapter.notifyDataSetChanged()
                        } else {
                            val index = adapter.itemCount
                            adapter.list.addAll(t.content)
                            val size = adapter.itemCount - index
                            adapter.notifyItemRangeChanged(index, size)
                        }
                    }
                }
                runnable?.run()
            }
        })

    private fun getLastId(): Int {
        if (adapter.list.isEmpty()) {
            return 0
        }
        return adapter.list.last().id
    }

    private fun pullUp() {
        if (adapter.pullup) return
        adapter.pullup = true
        post(getLastId(), false, Runnable {
            adapter.pullup = false
        })
    }

    private val itemClick = object : ListItemCall.OnImageClickListener {
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