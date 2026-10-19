package ru.fefu.dvfu.hero

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class HeroViewModel : ViewModel() {

    var uiState by mutableStateOf(HeroUiState(inventory = startItems()))
        private set

    fun onDamage() {
        updateHp(uiState.hp - 10)
    }

    fun onHeal() {
        if (uiState.isDefeated) return
        updateHp(uiState.hp + 10)
    }

    fun onUseItem(id: Int) {
        if (uiState.isDefeated) return
        val item = uiState.inventory.find { it.id == id } ?: return
        uiState = uiState.copy(inventory = uiState.inventory - item)
        updateHp(uiState.hp + item.heal)
    }

    fun onReset() {
        uiState = HeroUiState(inventory = startItems())
    }

    private fun updateHp(hp: Int) {
        uiState = uiState.copy(hp = hp.coerceIn(0, uiState.maxHp))
    }

    override fun onCleared() {
        Log.d("HeroViewModel", "onCleared")
    }
}
