package ru.fefu.dvfu.hero.ui

import ru.fefu.dvfu.hero.data.MAX_HP
import ru.fefu.dvfu.hero.model.Item

data class HeroUiState(
    val name: String = "Сэр Зелёный Плащ",
    val hp: Int = MAX_HP,
    val maxHp: Int = MAX_HP,
    val inventory: List<Item> = emptyList(),
) {
    val isDefeated: Boolean
        get() = hp == 0

    val isFullHp: Boolean
        get() = hp == maxHp
}
