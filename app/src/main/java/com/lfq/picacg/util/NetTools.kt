package com.lfq.picacg.util

import android.content.Context
import android.net.ConnectivityManager
import com.lfq.picacg.MyApplication
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val serverurl: String = "http://192.168.0.108:8080/"
const val head_source_url = serverurl + "image/source/head/"
const val head_small_url = serverurl + "image/small/head/"

class NetTools {
    companion object {
        private var retrofit: Retrofit? = null

        fun <T> create(clazz: Class<T>): T? {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(serverurl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit?.create(clazz)
        }

        /**
         * 检查网络连接
         */
        fun isNetWorkAvailable(): Boolean {
            val cm = MyApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            if (cm != null) {
                @Suppress("DEPRECATION")
                if (cm.activeNetworkInfo != null) {
                    return cm.activeNetworkInfo.isAvailable
                }
            }
            return false
        }

        /**
         * 检查网络连接，没有网络就弹出提示消息
         */
        fun isNetWorkAvailableShowMessage(): Boolean {
            return if (isNetWorkAvailable()) {
                true
            } else {
                toast("网络未连接，请检查你的网络")
                false
            }
        }
    }
}

