package ru.fefu.dvfu.hero.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.fefu.dvfu.hero.data.GameRepository
import ru.fefu.dvfu.hero.data.RoomGameRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun gameRepository(impl: RoomGameRepository): GameRepository
}
