package com.dacs.vku.presentation.notificationKHTC

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dacs.vku.domain.usecases.notification.NotiUseCases
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject

@HiltViewModel
class KHTCViewModel @Inject constructor(
    private val notiUseCases: NotiUseCases
):ViewModel(){
    val noti = notiUseCases.getNotiKHTC().cachedIn(viewModelScope)
}