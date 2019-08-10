package com.lfq.picacg.service

import com.lfq.picacg.data.SubmissionRequest
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface SubmissionService {

    /**
     * 投稿上传请求
     */
    @Multipart
    @POST("submission/upload")
    fun upload(
        @Query(value = "name") name: String,
        @Query(value = "key") key: Long,
        @Query(value = "title") title: String,
        @Query(value = "keyword") keyword: String,
        @Query(value = "introduction") introduction: String,
        @Part files: List<MultipartBody.Part>
    ): Call<SubmissionRequest>

    /**
     * 删除请求
     */
    @GET("submission/delete")
    fun delete(
        @Query(value = "name") name: String,
        @Query(value = "key") key: Long,
        @Query(value = "submission_id") submission_id: Int
    ): Call<Int>

    @GET("submission/update")
    fun update(
        @Query(value = "name") name: String,
        @Query(value = "key") key: Long,
        @Query(value = "submission_id") submission_id: Int,
        @Query(value = "title") title: String,
        @Query(value = "keyword") keyword: String,
        @Query(value = "introduction") introduction: String
    ): Call<SubmissionRequest>
}