package ru.fefu.dvfu.hero

import android.util.Log
import androidx.lifecycle.ViewModel

class HeroViewModel(private val repo: GameRepository) : ViewModel() {

    val uiState: HeroUiState
        get() = HeroUiState(hp = repo.hp, inventory = repo.items)

    fun onDamage() = repo.damage()

    fun onHeal() = repo.heal()

    fun onReset() = repo.reset()

    override fun onCleared() {
        Log.d("HeroViewModel", "onCleared")
    }
}
