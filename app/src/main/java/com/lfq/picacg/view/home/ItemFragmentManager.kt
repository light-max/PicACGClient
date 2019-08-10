package com.lfq.picacg.view.home

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.lfq.picacg.view.home.fragment.HomeFragment
import com.lfq.picacg.view.home.fragment.MineFragment
import com.lfq.picacg.view.home.fragment.RankingFragment

class ItemFragmentManager(private val activity: AppCompatActivity, private val containerId: Int) {
    private val fragments = arrayOf(
        HomeFragment(), RankingFragment(), MineFragment()
    )

    private var currentFragment: Fragment? = null

    fun switchFragment(item: Int) {
        val transaction = activity.supportFragmentManager.beginTransaction()
        val targetFragment = fragments[item]
        if (currentFragment != null) {
            transaction.hide(currentFragment!!)
        }
        if (targetFragment.isAdded) {
            transaction.show(targetFragment).commit()
        } else {
            transaction.add(containerId, targetFragment).commit()
        }
        currentFragment = targetFragment
    }

    companion object {
        private var manager: ItemFragmentManager? = null

        fun select(activity: AppCompatActivity, id: Int, item: Int) {
            if (manager == null) {
                manager = ItemFragmentManager(activity, id)
            }
            manager?.switchFragment(item)
        }
    }
}