package com.dacs.vku.ui.viewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dacs.vku.models.Notification
import com.dacs.vku.models.NotificationResponse
import com.dacs.vku.repository.NotificationRepository
import com.dacs.vku.util.Resources
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.http.Query
import java.io.IOException

class NotificationDaoTaoViewModel(
    app: Application,
    val notificationRepository: NotificationRepository
) : AndroidViewModel(app) {



    val notificationDaotao: MutableLiveData<Resources<MutableList<Notification>>> =
        MutableLiveData()

    var notificationDaoTaoPage = 1
    var notificationDaoTaoResponse: MutableList<Notification>? = null


    val searchNotification: MutableLiveData<Resources<MutableList<Notification>>> =
        MutableLiveData()
    var searchNotificationPage = 1
    var searchNotificationResponse: MutableList<Notification>? = null
    var notificationSearchQuery: String? = null
    var oldSearchQuery: String? = null
    var newSearchQuery: String? = null
    init {
        getNotificationDaoTao()
    }
    fun getNotificationDaoTao() = viewModelScope.launch {
        notificationDaoTaoInternet()
    }

    fun searchNotification(searchQuery: String) = viewModelScope.launch {
        searchNotificationDaoTaoInternet(searchQuery)
    }
     private fun handleNotificationDaoTao(response: Response<MutableList<Notification>>): Resources<MutableList<Notification>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                notificationDaoTaoPage++
                if (notificationDaoTaoResponse == null) {
                    notificationDaoTaoResponse = resultResponse
                } else {
                    notificationDaoTaoResponse?.addAll(resultResponse)

                }
                return Resources.Success(notificationDaoTaoResponse ?: resultResponse)
            }
        }
        return Resources.Error(response.message())

    }

    private fun handleSearchNotification(response: Response<MutableList<Notification>>): Resources<MutableList<Notification>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNotificationPage++
                if (searchNotificationResponse == null || newSearchQuery != oldSearchQuery) {
                    searchNotificationPage = 1
                    oldSearchQuery = newSearchQuery
                } else {
                    searchNotificationPage++
                    val oldNotification = searchNotificationResponse
                    val newNotification = resultResponse
                }
                return Resources.Success(searchNotificationResponse ?: resultResponse)
            }
        }
        return Resources.Error(response.message())

    }

    fun addToSaved(notification: Notification) = viewModelScope.launch {
        notificationRepository.upsert(notification)
    }

    fun getSaved() = notificationRepository.getSavedNotification()

    fun deleteSaved(notification: Notification) = viewModelScope.launch {
        notificationRepository.deleteNotification(notification)

    }

    fun internetConnection(context: Context): Boolean {
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
            return getNetworkCapabilities(activeNetwork)?.run {
                when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } ?: false
        }
    }

    suspend fun notificationDaoTaoInternet() {
        notificationDaotao.postValue(Resources.Loading())
        try {
            if (internetConnection(this.getApplication())) {
                val response = notificationRepository.getNotificationDaoTao()
                notificationDaotao.postValue(handleNotificationDaoTao(response))
            } else {
                notificationDaotao.postValue(Resources.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> notificationDaotao.postValue(Resources.Error("unable to connect"))
                else -> notificationDaotao.postValue(Resources.Error("No Signal"))
            }
        }
    }

    suspend fun searchNotificationDaoTaoInternet(searchQuery: String) {
        newSearchQuery = searchQuery
        searchNotification.postValue(Resources.Loading())

        try {
            if (internetConnection((this.getApplication()))) {
                val response = notificationRepository.searchNotification(searchQuery)
                searchNotification.postValue(handleSearchNotification(response))


            } else {
                searchNotification.postValue(Resources.Error("No Internet"))
            }
        } catch (t: Throwable) {
            when(t) {
                is IOException -> searchNotification.postValue(Resources.Error("Unable To Connect"))
                else -> searchNotification.postValue(Resources.Error("No Signal"))
            }

        }

    }

}