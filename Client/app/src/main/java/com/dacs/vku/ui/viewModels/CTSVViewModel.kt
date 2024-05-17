package com.dacs.vku.ui.viewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dacs.vku.models.Notification
import com.dacs.vku.repository.NotificationRepository
import com.dacs.vku.util.Resources
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class CTSVViewModel(
    app: Application,
    val notificationRepository: NotificationRepository
) : AndroidViewModel(app) {

    val notificationCTSV: MutableLiveData<Resources<MutableList<Notification>>> =
        MutableLiveData()
    var notificationCTSVPage = 1
    var notificationCTSVResponse: MutableList<Notification>? = null


    init {
        getNotificationCTSV()
    }
    fun getNotificationCTSV() = viewModelScope.launch {
        notificationCTSVInternet()
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

    suspend fun notificationCTSVInternet() {
        notificationCTSV.postValue(Resources.Loading())
        try {
            if (internetConnection(this.getApplication())) {
                val response = notificationRepository.getNotificationCTSV()
                notificationCTSV.postValue(handleNotificationCTSV(response))
            } else {
                notificationCTSV.postValue(Resources.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> notificationCTSV.postValue(Resources.Error("unable to connect"))
                else -> notificationCTSV.postValue(Resources.Error("No Signal"))
            }
        }
    }

    private fun handleNotificationCTSV(response: Response<MutableList<Notification>>): Resources<MutableList<Notification>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                notificationCTSVPage++
                if (notificationCTSVResponse == null) {
                    notificationCTSVResponse = resultResponse
                } else {
                    notificationCTSVResponse?.addAll(resultResponse)

                }
                return Resources.Success(notificationCTSVResponse ?: resultResponse)
            }
        }
        return Resources.Error(response.message())

    }

}