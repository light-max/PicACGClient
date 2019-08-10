package com.lfq.picacg.view.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.lfq.picacg.R
import com.lfq.picacg.call.MyNetCall
import com.lfq.picacg.data.SignInRequest
import com.lfq.picacg.data.UserInfoRequest
import com.lfq.picacg.service.imp.get_data
import com.lfq.picacg.service.imp.sign_in
import com.lfq.picacg.util.MyProgressDialog
import com.lfq.picacg.util.UserTools
import com.lfq.picacg.util.toast
import com.lfq.picacg.view.home.fragment.updateMineHeadImage
import com.lfq.picacg.view.home.fragment.updateMineUi


/**
 * 登录界面的布局
 */
class SignInFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.signin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        switched = view.findViewById(R.id.switched)
        switched.setOnClickListener(activity as View.OnClickListener)
        val post = view.findViewById<Button>(R.id.post)
        val name = view.findViewById<EditText>(R.id.name)
        val password = view.findViewById<EditText>(R.id.password)
        post.setOnClickListener {
            post(name.text.toString(), password.text.toString())
        }
    }

    lateinit var switched: TextView

    private fun post(name: String, password: String) {
        var dialog = MyProgressDialog(activity, "登录中...")
        //发送登录请求
        sign_in(name, password, object : MyNetCall<SignInRequest> {
            //请求结束
            override fun onRequest(t: SignInRequest?) {
                if (t != null) {
                    if (t.code == 0) {
                        UserTools.key = t.key
                        val check = view?.findViewById<CheckBox>(R.id.checkbox)
                        val flag: Boolean = check?.isChecked ?: false
                        UserTools.set(name, password, flag)
                        activity?.finish()
                        get_data(object : MyNetCall<UserInfoRequest> {
                            override fun onRequest(t: UserInfoRequest?) {
                                if (t != null) {
                                    when (t.code) {
                                        0 -> {
                                            UserTools.userinfo = t
                                            updateMineUi()
                                            updateMineHeadImage()
                                        }
                                        -1 -> toast("服务器异常")
                                    }
                                }
                            }
                        })
                    }
                    toast(t.message)
                }
                dialog.dismiss()
            }
        })
    }
}
