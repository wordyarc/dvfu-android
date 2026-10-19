package ru.fefu.dvfu.hero

data class Item(
    val id: Int,
    val name: String,
    val heal: Int,
)

data class HeroUiState(
    val name: String = "Сэр Зелёный Плащ",
    val hp: Int = 100,
    val maxHp: Int = 100,
    val inventory: List<Item> = emptyList(),
) {
    val isDefeated: Boolean
        get() = hp == 0

    val isFullHp: Boolean
        get() = hp == maxHp
}

fun startItems() = List(30) { i ->
    Item(i, "Зелье ${i + 1}", heal = 10 * (i % 3 + 1))
}
