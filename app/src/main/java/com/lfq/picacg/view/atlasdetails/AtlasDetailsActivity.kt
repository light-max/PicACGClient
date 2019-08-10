package com.lfq.picacg.view.atlasdetails

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.lfq.picacg.R
import com.lfq.picacg.data.ContentRequest
import com.lfq.picacg.view.ImageBrowse
import com.lfq.picacg.view.hideBottomUIMenu
import me.imid.swipebacklayout.lib.SwipeBackLayout
import me.imid.swipebacklayout.lib.app.SwipeBackActivity

class AtlasDetailsActivity : SwipeBackActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT)
        setContentView(R.layout.activity_atlasdetails)
        hideBottomUIMenu()
        val bean = intent.getParcelableExtra<ContentRequest.ContentBean>(DATA)
        this.bean = bean
        adapter.title = bean.title
        adapter.introduction = bean.introduction
        adapter.list.addAll(bean.show)
        val recycler: RecyclerView = findViewById(R.id.recycler) as RecyclerView
        recycler.adapter = adapter
    }

    var bean: ContentRequest.ContentBean? = null

    private val adapter = ImageListAdapter(this, object : ImageListAdapter.OnImageClickListener {
        override fun onClick(position: Int) {
            val intent = Intent(this@AtlasDetailsActivity, ImageBrowse::class.java)
            intent.putExtra("url", bean?.images?.get(position))
            startActivity(intent)
        }
    })

    companion object {
        val DATA = "ContentRequest.ContentBean"
    }
}