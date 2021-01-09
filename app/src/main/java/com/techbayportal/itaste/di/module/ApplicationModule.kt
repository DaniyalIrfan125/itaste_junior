package com.techbayportal.itaste.di.module

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.techbayportal.itaste.BuildConfig
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.data.local.db.AppDao
import com.techbayportal.itaste.data.local.db.AppDatabase
import com.techbayportal.itaste.data.remote.ApiService
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.techbayportal.itaste.data.local.datastore.DataStoreProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ApplicationModule {


    //added context for using chucker interceptor
    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext appContext: Context) =
        if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(ChuckerInterceptor.Builder(appContext).build())
            .build()
    } else OkHttpClient
        .Builder()
        .build()


    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(AppConstants.ApiConfiguration.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideDataStoreProvider(@ApplicationContext appContext: Context) =
        DataStoreProvider(appContext)

    @Singleton
    @Provides
    fun provideDao(db: AppDatabase) = db.appDao()

    @Singleton
    @Provides
    fun provideRepository(
        apiService: ApiService,
        localDataSource: AppDao
    ) =
        MainRepository(apiService, localDataSource)

}