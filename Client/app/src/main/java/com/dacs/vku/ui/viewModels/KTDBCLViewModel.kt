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

class KTDBCLViewModel(
    app: Application,
    val notificationRepository: NotificationRepository
) : AndroidViewModel(app) {

    val notificationKTDBCL: MutableLiveData<Resources<MutableList<Notification>>> =
        MutableLiveData()
    var notificationKTDBCLPage = 1
    var notificationKTDBCLResponse: MutableList<Notification>? = null


    init {
        getNotificationKTDBCL()
    }
    fun getNotificationKTDBCL() = viewModelScope.launch {
        notificationKTDBCLInternet()
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

    suspend fun notificationKTDBCLInternet() {
        notificationKTDBCL.postValue(Resources.Loading())
        try {
            if (internetConnection(this.getApplication())) {
                val response = notificationRepository.getNotificationKTDBCL()
                notificationKTDBCL.postValue(handleNotificationKTDBCL(response))
            } else {
                notificationKTDBCL.postValue(Resources.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> notificationKTDBCL.postValue(Resources.Error("unable to connect"))
                else -> notificationKTDBCL.postValue(Resources.Error("No Signal"))
            }
        }
    }

    private fun handleNotificationKTDBCL(response: Response<MutableList<Notification>>): Resources<MutableList<Notification>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                notificationKTDBCLPage++
                if (notificationKTDBCLResponse == null) {
                    notificationKTDBCLResponse = resultResponse
                } else {
                    notificationKTDBCLResponse?.addAll(resultResponse)

                }
                return Resources.Success(notificationKTDBCLResponse ?: resultResponse)
            }
        }
        return Resources.Error(response.message())

    }

}