package com.lfq.picacg.util

import com.lfq.picacg.call.MyNetCall
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 自定义的Callback回调
 * 重写了请求失败的接口，请求失败告诉用户网络连接异常
 */
class MyCallback<T> : Callback<T> {
    constructor(call: MyNetCall<T>) {
        this.call = call
    }

    constructor(call: MyNetCall<T>, showNotNetMessage: Boolean) : this(call) {
        this.isShowNotNetMessage = showNotNetMessage
    }

    private var call: MyNetCall<T>

    private var isShowNotNetMessage: Boolean = true

    override fun onResponse(call: Call<T>?, response: Response<T>) {
        this.call.onRequest(response.body())
    }

    override fun onFailure(call: Call<T>?, t: Throwable) {
        if (isShowNotNetMessage) {
            toast("网络连接失败，请检查你的网络设置")
        }
        t.printStackTrace()
        this.call.onRequest(null)
    }

    /**
     * 设置是否提示网络未连接
     */
    fun setShowMessage(showNotNetMessage: Boolean): MyCallback<T> {
        this.isShowNotNetMessage = showNotNetMessage
        return this
    }
}