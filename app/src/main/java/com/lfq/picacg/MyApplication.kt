package com.lfq.picacg

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.lfq.picacg.call.MyNetCall
import com.lfq.picacg.data.SignInRequest
import com.lfq.picacg.data.UserInfoRequest
import com.lfq.picacg.service.imp.LoginServiceInstance
import com.lfq.picacg.service.imp.get_data
import com.lfq.picacg.util.GetFlowUtil
import com.lfq.picacg.util.MyCallback
import com.lfq.picacg.util.UserTools
import com.lfq.picacg.util.toast
import com.lfq.picacg.view.home.fragment.updateMineUi

class MyApplication : Application() {
    companion object {
        lateinit var context: Context
        val TAG = "MyApplication"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT)
        autoSignIn()
        val info = GetFlowUtil.getAppFlowInfo("com.lfq.picacg", context)
        println(info.downkb + info.upkb)
    }

    /**
     * 尝试自动登录账号
     */
    fun autoSignIn() {
        if (UserTools.isAutoSignIn()) {
            val name = UserTools.getLocalName()
            val password = UserTools.getLocalPassword()
            LoginServiceInstance
                .getInstance()?.sign_in(name, password)?.enqueue(
                    MyCallback(AutoSignIn(), false)
                )
        }
    }

    /**
     * 登录请求的回调
     */
    private inner class AutoSignIn : MyNetCall<SignInRequest> {
        override fun onRequest(t: SignInRequest?) {
            //登录成功
            if (t != null && t.code == 0) {
                UserTools.key = t.key
                UserTools.set(UserTools.getLocalName(), UserTools.getLocalPassword())
                // 登录成功就发送获取个人数据的请求
                get_data(UserInfo())
                Log.e(TAG, "登录成功: " + UserTools.name)
            }
        }
    }

    /**
     * 获取个人数据请求的回调
     */
    private inner class UserInfo : MyNetCall<UserInfoRequest> {
        override fun onRequest(t: UserInfoRequest?) {
            if (t != null) {
                when (t.code) {
                    0 -> {
                        UserTools.userinfo = t
                        updateMineUi()
                    }
                    -1 -> toast("服务器异常")
                }
            }
        }
    }
}
