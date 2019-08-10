package com.lfq.picacg.service

import com.lfq.picacg.data.ContentRequest
import org.json.JSONArray
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ContentService {
    /**
     * 获取随机推送的请求
     */
    @GET(value = "content/random")
    fun rand(
        @Query("number") number: Int
    ): Call<ContentRequest>

    /**
     * 获取推送请求
     */
    @GET(value = "content/get")
    fun get(
        @Query("number") number: Int,
        @Query("filter_id") filter_id: JSONArray
    ): Call<ContentRequest>

    /**
     * 获取特定类型排序的稿件
     */
    @GET(value = "content/sort/{type}")
    fun sort(
        @Path("type") type: String,
        @Query("number") number: Int,
        @Query("lastid") lastid: Int
    ): Call<ContentRequest>

    /**
     * 获取指定作者的投稿
     */
    @GET(value = "content/get_author/{id}")
    fun get(
        @Path("id") id: Int,
        @Query("number") number: Int,
        @Query("lastid") lastid: Int
    ): Call<ContentRequest>

    /**
     * 获取指定的投稿
     */
    @GET(value = "content/get/{id}")
    fun get(@Path("id") id: Int): Call<ContentRequest.ContentBean>
}