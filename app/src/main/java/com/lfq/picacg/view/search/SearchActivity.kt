package com.lfq.picacg.view.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.lfq.picacg.R
import com.lfq.picacg.call.ListItemCall
import com.lfq.picacg.call.MyNetCall
import com.lfq.picacg.data.ContentRequest
import com.lfq.picacg.data.SearchRequest
import com.lfq.picacg.service.imp.get_content
import com.lfq.picacg.service.imp.search
import com.lfq.picacg.util.MyProgressDialog
import com.lfq.picacg.util.toast
import com.lfq.picacg.view.atlasdetails.AtlasDetailsActivity
import com.lfq.picacg.view.info.AuthorInfoActivity

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        findViewById<ImageView>(R.id.back).setOnClickListener { finish() }
        val content = findViewById<EditText>(R.id.content)
        content.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                post(content.text.toString())
            }
            false
        }
        layout = findViewById(R.id.layout)
        initView()
        setItemListener()
    }

    private lateinit var layout: View
    private val title = SearchContentFragment()
    private val keyword = SearchContentFragment()
    private val user = SearchContentFragment()

    private fun post(value: String) {
        val dialog = MyProgressDialog(this, "搜索中...")
        search(value, object : MyNetCall<SearchRequest> {
            override fun onRequest(t: SearchRequest?) {
                if (t != null) {
                    layout.visibility = View.VISIBLE
                    title.setBeans(t.title)
                    keyword.setBeans(t.keyword)
                    user.setBeans(t.user)
                    //刷新ui
                    val tab = findViewById<TabLayout>(R.id.table)
                    tab.getTabAt(0)?.text = "标题 (${t.title.size})"
                    tab.getTabAt(1)?.text = "关键字 (${t.keyword.size})"
                    tab.getTabAt(2)?.text = "用户名 (${t.user.size})"
                    val pager = findViewById<ViewPager>(R.id.viewpager)
                    when {
                        t.title.size != 0 -> pager.currentItem = 0
                        t.keyword.size != 0 -> pager.currentItem = 1
                        t.user.size != 0 -> pager.currentItem = 2
                    }
                }
                dialog.dismiss()
            }
        })
    }

    private fun initView() {
        val pager = findViewById<ViewPager>(R.id.viewpager)
        pager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return when (position) {
                    0 -> title
                    1 -> keyword
                    2 -> user
                    else -> getItem(position)
                }
            }

            override fun getCount(): Int {
                return 3
            }
        }
        val tab = findViewById<TabLayout>(R.id.table)
        tab.addTab(tab.newTab())
        tab.addTab(tab.newTab())
        tab.addTab(tab.newTab())
        tab.setupWithViewPager(pager)
    }

    private fun setItemListener() {
        val submissionlistener = object : ListItemCall.OnSearchResClickListener {
            override fun onnResClick(bean: SearchRequest.SearchBean) {
                val dialog = MyProgressDialog(this@SearchActivity, "正在打开...")
                get_content(bean.id, object : MyNetCall<ContentRequest.ContentBean> {
                    override fun onRequest(t: ContentRequest.ContentBean?) {
                        if (t != null) {
                            if (t.id == 0) {
                                toast("出错了")
                                return
                            }
                            val intent = Intent(this@SearchActivity, AtlasDetailsActivity::class.java)
                            intent.putExtra(AtlasDetailsActivity.DATA, t)
                            startActivity(intent)
                        }
                        dialog.dismiss()
                    }
                })
            }
        }
        title.adapter.listener = submissionlistener
        keyword.adapter.listener = submissionlistener
        user.adapter.listener = object : ListItemCall.OnSearchResClickListener {
            override fun onnResClick(bean: SearchRequest.SearchBean) {
                val intent = Intent(this@SearchActivity, AuthorInfoActivity::class.java)
                intent.putExtra(AuthorInfoActivity.AUTHOR_ID, bean.id)
                startActivity(intent)
            }
        }
    }
}