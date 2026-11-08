package ru.fefu.dvfu.hero.data

import ru.fefu.dvfu.hero.model.Item

private val potionIndexes = listOf(
    "potion-of-healing-common",
    "potion-of-healing-greater",
    "potion-of-healing-superior",
)

fun startItems() = List(30) { i ->
    val tier = i % 3
    Item(i, "Зелье ${i + 1}", heal = 10 * (tier + 1), apiIndex = potionIndexes[tier])
} + listOf(
    Item(30, "Мутное зелье", heal = 10, apiIndex = "potion-of-mystery"),
    Item(31, "Капризное зелье", heal = 10, apiIndex = SERVER_ERROR_INDEX),
)
