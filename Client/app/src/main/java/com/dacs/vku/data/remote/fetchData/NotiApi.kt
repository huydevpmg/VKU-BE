package com.dacs.vku.data.remote.fetchData

import com.dacs.vku.domain.model.NotiItem
import retrofit2.http.GET

interface NotiApi {
    @GET("crawlAndSaveDataDaoTao")
    suspend fun getNotificationsDaotao(
    ): List<NotiItem>

    @GET("crawlAndSaveCTSV")
    suspend fun getNotificationsCTSV(
    ): List<NotiItem>

    @GET("crawlAndSaveKHTC")
    suspend fun getNotificationsKHTC(
    ): List<NotiItem>

    @GET("crawlAndSaveKTDBCL")
    suspend fun getNotificationsKTDBCL(
    ): List<NotiItem>

}