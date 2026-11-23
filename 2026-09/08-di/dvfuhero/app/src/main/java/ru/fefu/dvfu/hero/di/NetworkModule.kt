package ru.fefu.dvfu.hero.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import ru.fefu.dvfu.hero.data.fakeServerError
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

const val BASE_URL = "https://www.dnd5eapi.co/api/2014/"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun okHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .callTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor { Log.d("HeroHttp", it) }.apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .addInterceptor(fakeServerError)
        .build()

    @Provides
    @Singleton
    fun retrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
}

private val json = Json { ignoreUnknownKeys = true }
