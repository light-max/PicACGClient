package com.lfq.picacg.service

import com.lfq.picacg.data.SearchRequest
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {
    /**
     * 搜索
     */
    @GET("search")
    fun search(@Query("value") value: String): Call<SearchRequest>
}