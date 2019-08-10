package com.lfq.picacg.view.login

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.lfq.picacg.R

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener { finish() }
        toolbar.title = "登录"
        switchFragment(signup, signin)
    }

    private lateinit var toolbar: Toolbar
    private var signin: SignInFragment = SignInFragment()
    private var signup: SignUpFragment = SignUpFragment()

    private fun switchFragment(currentFragment: Fragment, targetFragment: Fragment) {
        var beginTransaction = supportFragmentManager.beginTransaction()
        if (currentFragment.isAdded) {
            beginTransaction.hide(currentFragment)
        }
        if (targetFragment.isAdded) {
            beginTransaction.show(targetFragment)
        } else {
            beginTransaction.add(R.id.content, targetFragment)
        }
        beginTransaction.commit()
    }

    override fun onClick(view: View) {
        //当前在登录界面，切换到注册
        if (view == signin.switched) {
            toolbar.title = "注册"
            switchFragment(signin, signup)
        }
        //当前在注册界面，切换到登录
        else if (view == signup.switched) {
            toolbar.title = "登录"
            switchFragment(signup, signin)
        }
    }
}