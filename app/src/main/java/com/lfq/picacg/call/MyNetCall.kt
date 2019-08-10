package com.lfq.picacg.call;

/**
 * 封装网络请求的回调
 */
interface MyNetCall<T> {
    /**
     * 请求，如果请求失败，t 为null
     */
    fun onRequest(t: T?)
}
