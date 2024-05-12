package com.dacs.vku.data.remote.a_ktdbcl

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dacs.vku.data.remote.NotiApi
import com.dacs.vku.domain.model.NotiItem

class NotificationKTDBCLPagingSource(private val notiApi: NotiApi) : PagingSource<Int, NotiItem>() {
    override fun getRefreshKey(state: PagingState<Int, NotiItem>): Int? {
            TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NotiItem> {
        return try {
            val nextPageNumber = params.key ?: 1
            val notificationsKTDBCL = notiApi.getNotificationsKTDBCL()
            LoadResult.Page(
                data = notificationsKTDBCL,
                prevKey = null,
                nextKey = nextPageNumber + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}