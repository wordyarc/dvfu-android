package ru.fefu.dvfu.hero.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.fefu.dvfu.hero.data.db.HeroDatabase
import ru.fefu.dvfu.hero.data.db.buildDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun database(@ApplicationContext context: Context): HeroDatabase = buildDatabase(context)
}
