package com.dacs.vku.data.remote.fetchData.a_daotao

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dacs.vku.data.remote.fetchData.NotiApi
import com.dacs.vku.domain.model.NotiItem

class NotificationDaoTaoPagingSource(private val notiApi: NotiApi) : PagingSource<Int, NotiItem>() {
    override fun getRefreshKey(state: PagingState<Int, NotiItem>): Int? {
            TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NotiItem> {
        return try {
            val nextPageNumber = params.key ?: 1
            val notificationsDaoTao = notiApi.getNotificationsDaotao()
            LoadResult.Page(
                data = notificationsDaoTao,
                prevKey = null,
                nextKey = nextPageNumber + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}