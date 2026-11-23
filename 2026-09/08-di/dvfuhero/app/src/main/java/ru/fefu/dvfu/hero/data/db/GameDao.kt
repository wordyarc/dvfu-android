package ru.fefu.dvfu.hero.data.db

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Upsert

@Dao
interface GameDao {
    @Query("SELECT hp FROM hero WHERE id = 1")
    suspend fun hp(): Int?

    @Query("SELECT * FROM items ORDER BY id")
    suspend fun items(): List<ItemEntity>

    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun item(id: Int): ItemEntity?

    @Upsert
    suspend fun saveHero(hero: HeroEntity)

    @Insert
    suspend fun insertItems(items: List<ItemEntity>)

    @Query("DELETE FROM items WHERE id = :id")
    suspend fun deleteItem(id: Int)

    @Query("DELETE FROM items")
    suspend fun deleteAllItems()
}
