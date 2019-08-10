package com.lfq.picacg.view.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.lfq.picacg.R

class RankingFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.ranking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val pager = view.findViewById<ViewPager>(R.id.viewpager)
        pager.adapter = object : FragmentPagerAdapter(activity?.supportFragmentManager!!) {
            override fun getItem(position: Int): Fragment {
                return fragments[position]
            }

            override fun getCount(): Int {
                return 3
            }
        }
        val table = view.findViewById<TabLayout>(R.id.table)
        table.addTab(table.newTab())
        table.addTab(table.newTab())
        table.addTab(table.newTab())
        table.setupWithViewPager(pager)
        table.getTabAt(0)?.text = "最新发布"
        table.getTabAt(1)?.text = "最多点赞"
        table.getTabAt(2)?.text = "最多浏览"
    }

    private val fragments = arrayOf(
        RankingChildFragment("time"), RankingChildFragment("star"), RankingChildFragment("watch")
    )
}