package ru.fefu.dvfu.hero

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

const val MAX_HP = 100

// Помним пока делаем допущение чтот так ок. Но в будущем никакого mutableState в дата слое
class GameRepository {

    var hp by mutableStateOf(MAX_HP)
        private set

    var items by mutableStateOf(startItems())
        private set

    fun item(id: Int): Item? = items.find { it.id == id }

    fun damage() {
        updateHp(hp - 10)
    }

    fun heal() {
        if (hp == 0) return
        updateHp(hp + 10)
    }

    fun useItem(id: Int) {
        if (hp == 0) return
        val item = item(id) ?: return
        items = items - item
        updateHp(hp + item.heal)
    }

    fun reset() {
        hp = MAX_HP
        items = startItems()
    }

    private fun updateHp(value: Int) {
        hp = value.coerceIn(0, MAX_HP)
    }
}
