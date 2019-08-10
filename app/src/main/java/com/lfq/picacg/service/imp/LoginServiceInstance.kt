package com.lfq.picacg.service.imp

import com.lfq.picacg.call.MyNetCall
import com.lfq.picacg.data.SignInRequest
import com.lfq.picacg.data.SignUpRequest
import com.lfq.picacg.data.VerifyRequest
import com.lfq.picacg.service.LoginService
import com.lfq.picacg.service.imp.LoginServiceInstance.Companion.getInstance
import com.lfq.picacg.util.MyCallback
import com.lfq.picacg.util.NetTools

const val TAG = "LoginServiceInstance"

class LoginServiceInstance {
    companion object {
        private var service = NetTools.create(LoginService::class.java)

        fun getInstance(): LoginService? {
            return if (NetTools.isNetWorkAvailableShowMessage()) {
                service
            } else {
                null
            }
        }
    }
}

/**
 * 登录
 */
fun sign_in(name: String, password: String, netCall: MyNetCall<SignInRequest>) {
    getInstance()?.sign_in(name, password)?.enqueue(MyCallback(netCall))
}

/**
 * 拿到验证码
 */
fun create_verify_id(netCall: MyNetCall<VerifyRequest>) {
    getInstance()?.create_verify_id()?.enqueue(MyCallback(netCall))
}

/**
 * 注册
 */
fun sign_up(name: String, password: String, id: Long, answer: String, netCall: MyNetCall<SignUpRequest>) {
    getInstance()?.sign_up(name, password, id, answer)?.enqueue(MyCallback(netCall))
}

/**
 *登出
 */
fun sign_out(name: String, key: Long) {
    getInstance()?.sign_out(name, key)?.enqueue(MyCallback(object : MyNetCall<Any> {
        override fun onRequest(t: Any?) = Unit
    }, false))
}