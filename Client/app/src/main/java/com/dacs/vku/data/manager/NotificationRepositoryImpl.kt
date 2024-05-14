package com.dacs.vku.data.manager

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dacs.vku.data.remote.fetchData.NotiApi
import com.dacs.vku.data.remote.fetchData.a_ctsv.NotificationCTSVPagingSource
import com.dacs.vku.data.remote.fetchData.a_daotao.NotificationDaoTaoPagingSource
import com.dacs.vku.data.remote.fetchData.a_khtc.NotificationKHTCPagingSource
import com.dacs.vku.data.remote.fetchData.a_ktdbcl.NotificationKTDBCLPagingSource
import com.dacs.vku.domain.model.NotiItem
import com.dacs.vku.domain.repository.DaoTaoRepository
import kotlinx.coroutines.flow.Flow

class NotificationRepositoryImpl(private val notiApi: NotiApi): DaoTaoRepository {
    override fun getNotiDaoTao(): Flow<PagingData<NotiItem>> {

        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { NotificationDaoTaoPagingSource(notiApi) }
        ).flow
    }

    override fun getNotiCTSV(): Flow<PagingData<NotiItem>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { NotificationCTSVPagingSource(notiApi) }
        ).flow    }

    override fun getNotiKHTC(): Flow<PagingData<NotiItem>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { NotificationKHTCPagingSource(notiApi) }
        ).flow
    }

    override fun getNotiKTDBCL(): Flow<PagingData<NotiItem>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { NotificationKTDBCLPagingSource(notiApi) }
        ).flow
    }

}