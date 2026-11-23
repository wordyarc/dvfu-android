package ru.fefu.dvfu.hero.data

import ru.fefu.dvfu.hero.data.db.ItemEntity

private val potionIndexes = listOf(
    "potion-of-healing-common",
    "potion-of-healing-greater",
    "potion-of-healing-superior",
)

fun startItems() = List(30) { i ->
    val tier = i % 3
    ItemEntity(name = "Зелье ${i + 1}", heal = 10 * (tier + 1), apiIndex = potionIndexes[tier])
} + listOf(
    ItemEntity(name = "Мутное зелье", heal = 10, apiIndex = "potion-of-mystery"),
    ItemEntity(name = "Капризное зелье", heal = 10, apiIndex = SERVER_ERROR_INDEX),
)
