package com.lfq.picacg.service

import com.lfq.picacg.data.SignInRequest
import com.lfq.picacg.data.SignUpRequest
import com.lfq.picacg.data.VerifyRequest
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LoginService {
    /**
     * 登录请求
     */
    @GET(value = "login/sign_in")
    fun sign_in(
        @Query("name") name: String,
        @Query("password") password: String
    ): Call<SignInRequest>

    /**
     * 获取验证码
     */
    @GET(value = "login/create_verify_id")
    fun create_verify_id(): Call<VerifyRequest>

    /**
     * 注册请求
     */
    @GET(value = "login/sign_up")
    fun sign_up(
        @Query("name") name: String,
        @Query("password") password: String,
        @Query("id") id: Long,
        @Query("answer") answer: String
    ): Call<SignUpRequest>

    /**
     * 登出
     */
    @GET(value = "login/sign_out")
    fun sign_out(
        @Query("name") name: String,
        @Query("key") key: Long
    ): Call<Any>
}