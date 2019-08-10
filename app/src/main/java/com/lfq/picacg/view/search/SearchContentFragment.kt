package com.lfq.picacg.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lfq.picacg.data.SearchRequest

class SearchContentFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        recycler = RecyclerView(inflater.context)
        recycler.layoutManager = LinearLayoutManager(inflater.context)
        recycler.layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        return recycler
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler.adapter = adapter
    }

    lateinit var recycler: RecyclerView
    val adapter = SearchContentListAdapter()

    fun setBeans(bean: List<SearchRequest.SearchBean>) {
        adapter.list.clear()
        adapter.list.addAll(bean)
        adapter.notifyDataSetChanged()
    }
}