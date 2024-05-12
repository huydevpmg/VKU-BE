package com.dacs.vku.domain.usecases.notification

import androidx.paging.PagingData
import com.dacs.vku.domain.model.NotiItem
import com.dacs.vku.domain.repository.DaoTaoRepository
import kotlinx.coroutines.flow.Flow

class GetNotiKHTC(
    val notificationRepository: DaoTaoRepository

) {
    operator fun invoke(): Flow<PagingData<NotiItem>> {
        return notificationRepository.getNotiKHTC()
    }
}