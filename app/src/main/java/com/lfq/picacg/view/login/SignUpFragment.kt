package com.lfq.picacg.view.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.lfq.picacg.R
import com.lfq.picacg.call.MyNetCall
import com.lfq.picacg.data.SignUpRequest
import com.lfq.picacg.data.VerifyRequest
import com.lfq.picacg.service.imp.create_verify_id
import com.lfq.picacg.service.imp.sign_up
import com.lfq.picacg.util.MyProgressDialog
import com.lfq.picacg.util.toast

/**
 * 注册界面的布局
 */
class SignUpFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        switched = view.findViewById(R.id.switched)
        switched.setOnClickListener(activity as View.OnClickListener)
        view.findViewById<Button>(R.id.post)
            .setOnClickListener {
                val name = view.findViewById<EditText>(R.id.name)
                val pass = view.findViewById<EditText>(R.id.password)
                val pass2 = view.findViewById<EditText>(R.id.password2)
                val verification = view.findViewById<TextView>(R.id.verification)
                if (pass.text.toString() == pass2.text.toString()) {
                    post(name.text.toString(), pass.text.toString(), verification.text.toString())
                } else {
                    toast("两次输入的密码不一致")
                }
            }
        view.findViewById<TextView>(R.id.refresh)
            .setOnClickListener { refresh() }
        refresh()
    }

    lateinit var switched: TextView
    var id: Long = 0

    /**
     * 刷新验证码
     */
    fun refresh() {
        var dialog = MyProgressDialog(activity, "加载中...")
        create_verify_id(object : MyNetCall<VerifyRequest> {
            override fun onRequest(t: VerifyRequest?) {
                if (t != null) {
                    var verification = view?.findViewById<TextView>(R.id.problem)
                    verification?.text = t.problem
                    id = t.id
                }
                dialog.dismiss()
            }
        })
    }

    /**
     * 提交注册数据
     */
    fun post(name: String, pass: String, verification: String) {
        var dialog = MyProgressDialog(activity, "注册中...")
        sign_up(name, pass, id, verification, object : MyNetCall<SignUpRequest> {
            override fun onRequest(t: SignUpRequest?) {
                if (t != null) {
                    when (t.code) {
                        0 -> {
                            toast("注册成功快去登录吧")
                            //跳转到登录界面
                            switched.callOnClick()
                            //把原来的内容清空
                            var id = intArrayOf(R.id.name, R.id.password, R.id.password2, R.id.verification)
                            for (i in id) {
                                view?.findViewById<TextView>(i)?.text = ""
                            }
                        }
                        else -> {
                            toast(t.message);
                            refresh()
                        }
                    }
                }
                dialog.dismiss()
            }
        })
    }
}