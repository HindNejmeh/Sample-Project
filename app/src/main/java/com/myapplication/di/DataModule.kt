package com.myapplication.di

import com.myapplication.data.remote.DEFAULT_BASE_URL
import com.myapplication.data.remote.RemoteDataSource
import com.myapplication.data.remote.RemoteService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseUrl

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @BaseUrl
    fun provideBaseUrl(): String = DEFAULT_BASE_URL

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    fun provideRemoteService(remoteDataSource: RemoteDataSource): RemoteService =
        remoteDataSource.remoteService

}