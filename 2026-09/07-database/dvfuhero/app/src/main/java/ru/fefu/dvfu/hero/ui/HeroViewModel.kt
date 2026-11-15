package ru.fefu.dvfu.hero.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import ru.fefu.dvfu.hero.data.GameRepository

class HeroViewModel(private val repo: GameRepository) : ViewModel() {

    val uiState: HeroUiState?
        get() = repo.game?.let { HeroUiState(hp = it.hp, inventory = it.items) }

    fun onDamage() = repo.damage()

    fun onHeal() = repo.heal()

    fun onReset() = repo.reset()

    override fun onCleared() {
        Log.d("HeroViewModel", "onCleared")
    }
}
