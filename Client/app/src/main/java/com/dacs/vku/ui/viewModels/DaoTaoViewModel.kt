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

class DaoTaoViewModel(
    app: Application,
    val notificationRepository: NotificationRepository
) : AndroidViewModel(app) {

    val notificationDaotao: MutableLiveData<Resources<MutableList<Notification>>> =
        MutableLiveData()
    var notificationDaoTaoPage = 1
    var notificationDaoTaoResponse: MutableList<Notification>? = null


    init {
        getNotificationDaoTao()
    }
    fun getNotificationDaoTao() = viewModelScope.launch {
        notificationDaoTaoInternet()
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

}