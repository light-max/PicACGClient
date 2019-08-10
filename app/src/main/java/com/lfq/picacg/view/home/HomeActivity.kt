package com.lfq.picacg.view.home;

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lfq.picacg.R
import com.lfq.picacg.R.id
import com.lfq.picacg.view.search.SearchActivity
import com.lfq.picacg.view.submission.SubmissionActivity

class HomeActivity : AppCompatActivity() {
    val TAG = "HomeActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initAppToolBar()
        initNavigationView()
        ItemFragmentManager.select(this, id.container, 0)
    }

    private fun initAppToolBar() {
        val bar = findViewById<Toolbar>(id.toolbar)
        setSupportActionBar(bar)
    }

    private fun initNavigationView() {
        val nav = findViewById<BottomNavigationView>(id.nav)
        nav.setOnNavigationItemSelectedListener { listener ->
            val item = when (listener.itemId) {
                id.home -> 0
                id.ranking -> 1
                id.mine -> 2
                else -> -1
            }
            ItemFragmentManager.select(this, id.container, item)
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            id.upload -> startActivity(Intent(this, SubmissionActivity::class.java))
            id.search -> startActivity(Intent(this, SearchActivity::class.java))
        }
        return false
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val home = Intent(Intent.ACTION_MAIN)
            home.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            home.addCategory(Intent.CATEGORY_HOME)
            startActivity(home)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
