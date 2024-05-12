package com.dacs.vku.domain.usecases.notification

import androidx.paging.PagingData
import com.dacs.vku.domain.model.NotiItem
import com.dacs.vku.domain.repository.DaoTaoRepository
import kotlinx.coroutines.flow.Flow

class GetNotiKTDBCL(
    val notificationRepository: DaoTaoRepository

) {
    operator fun invoke(): Flow<PagingData<NotiItem>> {
        return notificationRepository.getNotiKTDBCL()
    }
}