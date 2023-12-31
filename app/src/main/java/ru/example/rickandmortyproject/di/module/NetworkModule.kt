package ru.example.rickandmortyproject.di.module

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.example.rickandmortyproject.data.characters.api.CharactersApi
import ru.example.rickandmortyproject.data.episodes.api.EpisodesApi
import ru.example.rickandmortyproject.data.locations.api.LocationsApi
import ru.example.rickandmortyproject.di.scope.ActivityScope
import ru.example.rickandmortyproject.utils.interceptors.AppChuckerInterceptor
import ru.example.rickandmortyproject.utils.interceptors.ConnectivityInterceptor
import ru.example.rickandmortyproject.utils.interceptors.NetworkExceptionInterceptor

private const val BASE_URL = "https://rickandmortyapi.com/api/"

@Module
class NetworkModule {
    @Provides
    @ActivityScope
    fun provideRetrofit(
        client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @ActivityScope
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
    @ActivityScope
    fun providesChuckerInterceptor(context: Context) =
        AppChuckerInterceptor(context).intercept()

    @Provides
    @ActivityScope
    fun providesNetworkExceptionInterceptor() =
        NetworkExceptionInterceptor()

    @Provides
    @ActivityScope
    fun providesConnectivityInterceptor(context: Context) =
        ConnectivityInterceptor(context)

    @Provides
    @ActivityScope
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @ActivityScope
    fun providesCharacterListNetworkSource(retrofit: Retrofit): CharactersApi =
        retrofit.create(CharactersApi::class.java)

    @Provides
    @ActivityScope
    fun providesEpisodesListNetworkSource(retrofit: Retrofit): EpisodesApi =
        retrofit.create(EpisodesApi::class.java)

    @Provides
    @ActivityScope
    fun provideLocationsListNetworkSource(retrofit: Retrofit): LocationsApi =
        retrofit.create(LocationsApi::class.java)
}
