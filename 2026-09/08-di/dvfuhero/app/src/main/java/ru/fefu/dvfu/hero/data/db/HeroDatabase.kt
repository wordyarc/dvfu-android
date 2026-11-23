package ru.fefu.dvfu.hero.data.db

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase

@Database(entities = [ItemEntity::class, HeroEntity::class], version = 1)
abstract class HeroDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
}

fun buildDatabase(context: Context): HeroDatabase =
    Room.databaseBuilder<HeroDatabase>(context, "hero.db").build()
