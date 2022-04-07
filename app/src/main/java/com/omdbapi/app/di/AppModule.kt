package com.omdbapi.app.di

import com.omdbapi.app.network.base.ApiConstants
import com.omdbapi.app.utils.functional.BuildUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * @author Nitin Khanna
 * @date 07/04/2022
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideNetworkingInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest: Request = chain.request()
            val requestWithUserAgent: Request = originalRequest.newBuilder()
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36")
                .build()
            chain.proceed(requestWithUserAgent)
        }
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(
        networkInterceptor: Interceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val client = OkHttpClient.Builder()
        if (BuildUtils.isDebug()) {
            client.addInterceptor(loggingInterceptor)
        }
        client.addNetworkInterceptor(networkInterceptor)
        client.connectTimeout(10, TimeUnit.SECONDS)
        client.readTimeout(30, TimeUnit.SECONDS)
        return client.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_STAGE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}