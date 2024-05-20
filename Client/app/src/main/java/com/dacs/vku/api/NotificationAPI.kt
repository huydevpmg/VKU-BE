package com.dacs.vku.api

import com.dacs.vku.models.Notification
import com.dacs.vku.models.NotificationResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface NotificationAPI {
    @GET("dao-tao")
    suspend fun getNotificationDaoTao(): Response<MutableList<Notification>>

    @GET("ctsv")
    suspend fun getNotificationCTSV(): Response<MutableList<Notification>>

    @GET("khtc")
    suspend fun getNotificationKHTC(): Response<MutableList<Notification>>

    @GET("ktdbcl")
    suspend fun getNotificationKTDBCL(): Response<MutableList<Notification>>

    @GET("search")
    suspend fun searchForNotification(
        @Query("title")
        searchQuery: String
    ): Response<MutableList<Notification>>

    @POST("/verify-id-token")
    fun verifyToken(@Body tokenRequest: TokenRequest): Call<ResponseBody>
}

data class TokenRequest(val token: String?)
