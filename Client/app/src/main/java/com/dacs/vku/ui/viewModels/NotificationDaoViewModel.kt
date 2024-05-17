package com.dacs.vku.ui.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dacs.vku.models.Notification
import com.dacs.vku.repository.NotificationRepository
import kotlinx.coroutines.launch

class NotificationDaoViewModel(
    app: Application,
    val notificationRepository: NotificationRepository
) : AndroidViewModel(app)  {

    fun addToSaved(notification: Notification) = viewModelScope.launch {
        notificationRepository.upsert(notification)
    }

    fun getSaved() = notificationRepository.getSavedNotification()

    fun deleteSaved(notification: Notification) = viewModelScope.launch {
        notificationRepository.deleteNotification(notification)

    }

}