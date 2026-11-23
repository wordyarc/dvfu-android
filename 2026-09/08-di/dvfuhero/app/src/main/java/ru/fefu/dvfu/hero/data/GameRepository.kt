package ru.fefu.dvfu.hero.data

import ru.fefu.dvfu.hero.model.Game
import ru.fefu.dvfu.hero.model.Item
import ru.fefu.dvfu.hero.model.ItemDetails

const val MAX_HP = 100

interface GameRepository {
    val game: Game?

    suspend fun item(id: Int): Item?

    suspend fun itemDetails(item: Item): ItemDetails

    fun damage()

    fun heal()

    fun reset()

    fun useItem(id: Int)
}


class FakeGmaeRepo(override val game: Game?) : GameRepository {
    override suspend fun item(id: Int): Item? {
        TODO("Not yet implemented")
    }

    override suspend fun itemDetails(item: Item): ItemDetails {
        TODO("Not yet implemented")
    }

    override fun damage() {
        TODO("Not yet implemented")
    }

    override fun heal() {
        TODO("Not yet implemented")
    }

    override fun reset() {
        TODO("Not yet implemented")
    }

    override fun useItem(id: Int) {
        TODO("Not yet implemented")
    }
}