package com.dacs.vku.domain.repository

import androidx.paging.PagingData
import com.dacs.vku.domain.model.NotiItem
import kotlinx.coroutines.flow.Flow


interface DaoTaoRepository {

    fun getNotiDaoTao(): Flow<PagingData<NotiItem>>
    fun getNotiCTSV(): Flow<PagingData<NotiItem>>
    fun getNotiKHTC(): Flow<PagingData<NotiItem>>
    fun getNotiKTDBCL(): Flow<PagingData<NotiItem>>
}
