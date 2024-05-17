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

class KHTCViewModel(
    app: Application,
    val notificationRepository: NotificationRepository
) : AndroidViewModel(app) {

    val notificationKHTC: MutableLiveData<Resources<MutableList<Notification>>> =
        MutableLiveData()
    var notificationKHTCPage = 1
    var notificationKHTCResponse: MutableList<Notification>? = null


    init {
        getNotificationKHTC()
    }
    fun getNotificationKHTC() = viewModelScope.launch {
        notificationKHTCInternet()
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

    suspend fun notificationKHTCInternet() {
        notificationKHTC.postValue(Resources.Loading())
        try {
            if (internetConnection(this.getApplication())) {
                val response = notificationRepository.getNotificationKHTC()
                notificationKHTC.postValue(handleNotificationKHTC(response))
            } else {
                notificationKHTC.postValue(Resources.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> notificationKHTC.postValue(Resources.Error("unable to connect"))
                else -> notificationKHTC.postValue(Resources.Error("No Signal"))
            }
        }
    }

    private fun handleNotificationKHTC(response: Response<MutableList<Notification>>): Resources<MutableList<Notification>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                notificationKHTCPage++
                if (notificationKHTCResponse == null) {
                    notificationKHTCResponse = resultResponse
                } else {
                    notificationKHTCResponse?.addAll(resultResponse)

                }
                return Resources.Success(notificationKHTCResponse ?: resultResponse)
            }
        }
        return Resources.Error(response.message())

    }

}