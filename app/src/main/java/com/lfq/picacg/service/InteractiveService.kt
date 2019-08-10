package com.lfq.picacg.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface InteractiveService {
    /**
     * 点赞
     */
    @GET(value = "interactive/star/{manuscriptid}")
    fun star(
        @Path("manuscriptid") manuscriptid: Int,
        @Query("name") name: String,
        @Query("key") key: Long
    ): Call<Int>

    /**
     * 增加浏览量
     */
    @GET(value = "interactive/watch/{manuscriptid}")
    fun watch(
        @Path("manuscriptid") manuscriptid: Int,
        @Query("name") name: String,
        @Query("key") key: Long
    ): Call<Int>
}