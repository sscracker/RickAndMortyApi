package ru.example.rickandmortyproject.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.chuckerteam.chucker.api.ChuckerInterceptor
import ru.example.rickandmortyproject.data.characters.list.api.CharactersApi
import ru.example.rickandmortyproject.utils.interceptors.AppChuckerInterceptor
import ru.example.rickandmortyproject.utils.interceptors.ConnectivityInterceptor
import ru.example.rickandmortyproject.utils.interceptors.NetworkExceptionInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val BASE_URL = "https://rickandmortyapi.com/api/"

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun providesOkHttpClient(
        chuckerInterceptor: ChuckerInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        networkExceptionInterceptor: NetworkExceptionInterceptor,
        connectivityInterceptor: ConnectivityInterceptor
    ): OkHttpClient = OkHttpClient.Builder().writeTimeout(1, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .callTimeout(1, TimeUnit.MINUTES)
        .retryOnConnectionFailure(true)
        .addInterceptor(chuckerInterceptor)
        .addInterceptor(networkExceptionInterceptor)
        .addInterceptor(connectivityInterceptor)
        .addInterceptor(httpLoggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun providesChuckerInterceptor(context: Context) =
        AppChuckerInterceptor(context).intercept()

    @Provides
    @Singleton
    fun providesNetworkExceptionInterceptor() =
        NetworkExceptionInterceptor()

    @Provides
    @Singleton
    fun providesConnectivityInterceptor(context: Context) =
        ConnectivityInterceptor(context)

    @Provides
    @Singleton
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun providesCharacterListNetworkSource(retrofit: Retrofit): CharactersApi =
        retrofit.create(CharactersApi::class.java)
}