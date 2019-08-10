package com.lfq.picacg.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.lfq.picacg.R
import com.lfq.picacg.call.MyNetCall
import com.lfq.picacg.data.SetPasswordRequest
import com.lfq.picacg.service.imp.set_password
import com.lfq.picacg.util.MyProgressDialog
import com.lfq.picacg.util.UserTools
import com.lfq.picacg.util.toast
import com.lfq.picacg.view.home.fragment.updateMineUi
import com.lfq.picacg.view.info.finishUserInfoActivity
import com.lfq.picacg.view.login.LoginActivity

class PasswordSetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setpassword)
        findViewById<Toolbar>(R.id.toolbar)
            .setNavigationOnClickListener {
                finish()
            }
        findViewById<Button>(R.id.post)
            .setOnClickListener {
                val source = findViewById<EditText>(R.id.source)
                val pass = findViewById<EditText>(R.id.password)
                val pass2 = findViewById<EditText>(R.id.password2)
                if (pass.text.toString() != pass2.text.toString()) {
                    toast("两次输入的密码不一致")
                } else {
                    post(source.text.toString(), pass.text.toString())
                }
            }
    }

    /**
     * 提交修改密码的请求
     */
    private fun post(source: String, password: String) {
        var dialog = MyProgressDialog(this, "修改中...")
        set_password(source, password, object : MyNetCall<SetPasswordRequest> {
            override fun onRequest(t: SetPasswordRequest?) {
                if (t != null) {
                    if (t.code == 0) {
                        UserTools.sign_out()
                        UserTools.clearbuffer()
                        updateMineUi()
                        finishUserInfoActivity()
                        finish()
                        startActivity(Intent(this@PasswordSetActivity, LoginActivity::class.java))
                    }
                    toast(t.message)
                }
                dialog.dismiss()
            }
        })
    }
}