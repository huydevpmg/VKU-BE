package com.dacs.vku.api

import com.dacs.vku.models.Notification
import com.dacs.vku.models.NotificationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NotificationAPI {
    @GET("crawlAndSaveDataDaoTao")
    suspend fun getNotificationDaoTao() : Response<MutableList<Notification>>

    @GET("search")
    suspend fun searchForNotification(
        @Query("title")
        searchQuery: String
    ): Response<MutableList<Notification>>

}