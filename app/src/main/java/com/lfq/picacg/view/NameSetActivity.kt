package com.lfq.picacg.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.lfq.picacg.R
import com.lfq.picacg.call.MyNetCall
import com.lfq.picacg.data.SetNameRequest
import com.lfq.picacg.service.imp.set_name
import com.lfq.picacg.util.MyProgressDialog
import com.lfq.picacg.util.UserTools
import com.lfq.picacg.util.toast
import com.lfq.picacg.view.home.fragment.updateMineUi
import com.lfq.picacg.view.info.finishUserInfoActivity
import com.lfq.picacg.view.login.LoginActivity

/**
 * 设置登录名
 */
class NameSetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setname)
        val name = findViewById<EditText>(R.id.name)
        findViewById<Toolbar>(R.id.toolbar)
            .setNavigationOnClickListener {
                finish()
            }
        findViewById<Button>(R.id.post)
            .setOnClickListener {
                post(name.text.toString())
            }
        name.setText(UserTools.name)
    }

    /**
     * 提交修改请求
     */
    private fun post(name: String) {
        var dialog = MyProgressDialog(this, "请稍后...")
        set_name(name, object : MyNetCall<SetNameRequest> {
            override fun onRequest(t: SetNameRequest?) {
                if (t != null) {
                    if (t.code == 0) {
                        UserTools.sign_out()
                        UserTools.clearbuffer()
                        updateMineUi()
                        finishUserInfoActivity()
                        finish()
                        startActivity(Intent(this@NameSetActivity, LoginActivity::class.java))
                    }
                    toast(t.message + "请重试")
                }
                dialog.dismiss()
            }
        })
    }
}
