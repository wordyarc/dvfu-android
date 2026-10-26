package ru.fefu.dvfu.hero

import android.util.Log
import androidx.lifecycle.ViewModel

data class ItemUiState(
    val item: Item?,
    val canUse: Boolean,
)

class ItemViewModel(
    private val itemId: Int,
    private val repo: GameRepository,
) : ViewModel() {

    private val item = repo.item(itemId)

    init {
        Log.d("ItemViewModel", "created for item $itemId")
    }

    val uiState: ItemUiState
        get() = ItemUiState(
            item = item,
            canUse = repo.hp < MAX_HP && repo.item(itemId) != null,
        )

    fun onUse() = repo.useItem(itemId)

    override fun onCleared() {
        Log.d("ItemViewModel", "onCleared for item $itemId")
    }
}
