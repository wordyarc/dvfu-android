package ru.fefu.dvfu.hero.data

import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

class SlowHeroApi(private val real: HeroApi) : HeroApi {

    override suspend fun magicItem(index: String): MagicItemDto {
        delay(2.seconds)
        return real.magicItem(index)
    }
}
