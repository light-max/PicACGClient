package com.lfq.picacg.util;

import android.content.SharedPreferences
import com.lfq.picacg.MyApplication
import com.lfq.picacg.data.UserInfoRequest

class UserTools {
    companion object {
        var key: Long = 0
        var name: String = ""
        var password: String = ""
        var userinfo: UserInfoRequest? = null

        private var preferences: SharedPreferences? = null

        private fun getPreferences(): SharedPreferences? {
            if (preferences == null) {
                preferences = MyApplication.context.getSharedPreferences("user", 0)
            }
            return preferences
        }

        fun isAutoSignIn(): Boolean {
            return getPreferences()?.getBoolean("auto", false) ?: false
        }

        fun setAutoSignIn(auto: Boolean) {
            getPreferences()!!.edit().putBoolean("auto", auto).apply()
        }

        fun getLocalName(): String {
            return getPreferences()?.getString("name", "") ?: ""
        }

        fun getLocalPassword(): String {
            return getPreferences()?.getString("password", "") ?: ""
        }

        fun set(name: String, password: String, auto: Boolean) {
            val edit = getPreferences()?.edit()
            edit?.putBoolean("auto", auto)
            if (auto) {
                edit?.putString("name", name)
                edit?.putString("password", password)
            } else {
                edit?.putString("name", "")
                edit?.putString("password", "")
            }
            edit?.apply()
            set(name, password)
        }

        fun clearbuffer() {
            getPreferences()!!.edit()
                .putBoolean("auto", false)
                .putString("name", "")
                .putString("password", "")
                .apply()
        }

        fun set(name: String, password: String) {
            this.name = name
            this.password = password
        }

        fun sign_out() {
            userinfo = null
            name = ""
            password = ""
            key = 0L
        }
    }
}
