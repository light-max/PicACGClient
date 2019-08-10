package com.lfq.picacg.service.imp

import com.lfq.picacg.call.MyNetCall
import com.lfq.picacg.data.AuthorInfoRequest
import com.lfq.picacg.data.SetNameRequest
import com.lfq.picacg.data.SetPasswordRequest
import com.lfq.picacg.data.UserInfoRequest
import com.lfq.picacg.service.UserService
import com.lfq.picacg.util.MyCallback
import com.lfq.picacg.util.NetTools
import com.lfq.picacg.util.UserTools
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File

class UserServiceInstance {
    companion object {
        const val TAG = "LoginServiceInstance"

        private var service = NetTools.create(UserService::class.java)

        fun getInstance(): UserService? {
            return if (NetTools.isNetWorkAvailableShowMessage()) {
                service
            } else {
                null
            }
        }
    }
}

/**
 * 获取用户信息
 */
fun get_data(netCall: MyNetCall<UserInfoRequest>) {
    UserServiceInstance.getInstance()?.get_data(UserTools.name, UserTools.key)?.enqueue(MyCallback(netCall))
}

/**
 * 修改用户信息
 */
fun set_data(obj: JSONObject, netCall: MyNetCall<Int>) {
    UserServiceInstance.getInstance()?.set_data(UserTools.name, UserTools.key, obj.toString())
        ?.enqueue(MyCallback(netCall))
}

/**
 * 修改登录名
 */
fun set_name(name: String, netCall: MyNetCall<SetNameRequest>) {
    UserServiceInstance.getInstance()?.set_name(UserTools.name, UserTools.key, name)?.enqueue(MyCallback(netCall))
}

/**
 * 修改登录密码
 */
fun set_password(source: String, password: String, netCall: MyNetCall<SetPasswordRequest>) {
    UserServiceInstance.getInstance()?.set_password(UserTools.name, UserTools.key, source, password)
        ?.enqueue(MyCallback(netCall))
}

/**
 * 上传头像
 */
fun set_head(file: File, netCall: MyNetCall<Int>) {
    val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
    val part = MultipartBody.Part.createFormData("file", file.name, requestBody)
    UserServiceInstance.getInstance()?.set_head(UserTools.name, UserTools.key, part)?.enqueue(MyCallback(netCall))
}

/**
 * 作者信息
 */
fun get_authorinfo(id: Int, netCall: MyNetCall<AuthorInfoRequest>) {
    UserServiceInstance.getInstance()?.get_authorinfo(id)?.enqueue(MyCallback(netCall))
}