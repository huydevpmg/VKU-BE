package com.dacs.vku.di

import android.app.Application
import com.dacs.vku.data.manager.LocalUserManagerImplementation
import com.dacs.vku.data.manager.NotificationRepositoryImpl
import com.dacs.vku.data.remote.NotiApi
import com.dacs.vku.domain.manager.LocalUserManager
import com.dacs.vku.domain.repository.DaoTaoRepository
import com.dacs.vku.domain.usecases.app_entry.AppEntryUseCases
import com.dacs.vku.domain.usecases.app_entry.ReadAppEntry
import com.dacs.vku.domain.usecases.app_entry.SaveAppEntry
import com.dacs.vku.domain.usecases.notification.GetNotiCTSV
import com.dacs.vku.domain.usecases.notification.GetNotiDaoTao
import com.dacs.vku.domain.usecases.notification.GetNotiKHTC
import com.dacs.vku.domain.usecases.notification.GetNotiKTDBCL
import com.dacs.vku.domain.usecases.notification.NotiUseCases
import com.dacs.vku.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    //mỗi single ton thể hienej mỗi dependencies khác nhau
    @Provides
    @Singleton
    fun provideLocalUserManger(
        application: Application,
    ): LocalUserManager = LocalUserManagerImplementation(context = application)


    @Provides
    @Singleton
    fun provideAppEntryUseCases(
        localUserManger: LocalUserManager,
    ): AppEntryUseCases = AppEntryUseCases(
        readAppEntry = ReadAppEntry(localUserManger),
        saveAppEntry = SaveAppEntry(localUserManger)
    )
    @Provides
    @Singleton
    fun provideNotiApi(): NotiApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NotiApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNotiRepository(notiApi: NotiApi): DaoTaoRepository = NotificationRepositoryImpl(notiApi)

    @Provides
    @Singleton
    fun provideNotiUseCases(notificationRepository: DaoTaoRepository): NotiUseCases{
        return NotiUseCases(
            getNotiDaoTao = GetNotiDaoTao(notificationRepository),
            getNotiCTSV = GetNotiCTSV(notificationRepository),
            getNotiKHTC = GetNotiKHTC(notificationRepository),
            getNotiKTDBCL =  GetNotiKTDBCL(notificationRepository)
        )
    }



}