package com.lfq.picacg.service

import com.lfq.picacg.data.AuthorInfoRequest
import com.lfq.picacg.data.SetNameRequest
import com.lfq.picacg.data.SetPasswordRequest
import com.lfq.picacg.data.UserInfoRequest
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface UserService {

    /**
     * 获取用户数据
     */
    @GET(value = "user/get/data")
    fun get_data(
        @Query("name") name: String,
        @Query("key") key: Long
    ): Call<UserInfoRequest>

    /**
     * 设置用户信息
     */
    @GET(value = "user/set/data")
    fun set_data(
        @Query("name") name: String,
        @Query("key") key: Long,
        @Query("args") args: String
    ): Call<Int>

    /**
     * 修改登录名
     */
    @GET(value = "user/set/name")
    fun set_name(
        @Query("name") name: String,
        @Query("key") key: Long,
        @Query("args") args: String
    ): Call<SetNameRequest>

    /**
     * 修改登录密码
     */
    @GET(value = "user/set/password")
    fun set_password(
        @Query("name") name: String,
        @Query("key") key: Long,
        @Query("password") password: String,
        @Query("args") args: String
    ): Call<SetPasswordRequest>

    /**
     * 修改头像
     */
    @Multipart
    @POST(value = "user/set/head")
    fun set_head(
        @Query("name") name: String,
        @Query("key") key: Long,
        @Part file: MultipartBody.Part
    ): Call<Int>

    /**
     * 获取作者信息
     */
    @GET(value = "user/get/authorinfo/{id}")
    fun get_authorinfo(
        @Path("id") id: Int
    ): Call<AuthorInfoRequest>
}