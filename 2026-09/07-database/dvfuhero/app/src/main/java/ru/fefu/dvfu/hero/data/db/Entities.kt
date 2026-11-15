package ru.fefu.dvfu.hero.data.db

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val heal: Int,
    val apiIndex: String,
)

@Entity(tableName = "hero")
data class HeroEntity(
    @PrimaryKey val id: Int = 1,
    val hp: Int,
)
