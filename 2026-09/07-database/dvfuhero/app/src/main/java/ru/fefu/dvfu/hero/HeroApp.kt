package ru.fefu.dvfu.hero

import android.app.Application
import android.util.Log
import kotlinx.coroutines.MainScope
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import ru.fefu.dvfu.hero.data.GameRepository
import ru.fefu.dvfu.hero.data.HeroApi
import ru.fefu.dvfu.hero.data.db.buildDatabase
import ru.fefu.dvfu.hero.data.fakeServerError
import ru.fefu.dvfu.hero.data.slowNetwork
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://www.dnd5eapi.co/api/2014/"

private const val SLOW_NETWORK = false

class HeroApp : Application() {

    private val json = Json { ignoreUnknownKeys = true }

    private val client = OkHttpClient.Builder()
        .callTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor { Log.d("HeroHttp", it) }.apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .apply { if (SLOW_NETWORK) addInterceptor(slowNetwork) }
        .addInterceptor(fakeServerError)
        .build()

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(HeroApi::class.java)

    private val appScope = MainScope()

    private val db by lazy { buildDatabase(this) }

    val repository by lazy { GameRepository(api, db, appScope) }

    override fun onCreate() {
        super.onCreate()
        repository.start()
    }
}
