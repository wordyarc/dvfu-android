package ru.fefu.dvfu.hero.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.fefu.dvfu.hero.data.HeroApi
import ru.fefu.dvfu.hero.data.SlowHeroApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun heroApi(retrofit: Retrofit): HeroApi = SlowHeroApi(retrofit.create(HeroApi::class.java))
}
