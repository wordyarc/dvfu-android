package ru.fefu.dvfu.hero.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.fefu.dvfu.hero.data.GameRepository
import ru.fefu.dvfu.hero.data.MAX_HP
import ru.fefu.dvfu.hero.model.ItemLoadException
import ru.fefu.dvfu.hero.model.ItemNotFoundException

class ItemViewModel(
    private val itemId: Int,
    private val repo: GameRepository,
) : ViewModel() {

    var uiState: ItemUiState by mutableStateOf(ItemUiState.Loading)
        private set

    val canUse: Boolean
        get() = repo.hp < MAX_HP && repo.item(itemId) != null

    private var loadJob: Job? = null

    init {
        Log.d("ItemViewModel", "created for item $itemId")
        load()
    }

    fun onRetry() = load()

    fun onUse() = repo.useItem(itemId)

    private fun load() {
        if (loadJob?.isActive == true) return
        loadJob = viewModelScope.launch {
            uiState = ItemUiState.Loading
            uiState = fetchState()
        }
    }

    private suspend fun fetchState(): ItemUiState {
        val item = repo.item(itemId) ?: return ItemUiState.NotFound
        return try {
            ItemUiState.Content(item, repo.itemDetails(item))
        } catch (e: ItemNotFoundException) {
            ItemUiState.NotFound
        } catch (e: ItemLoadException) {
            ItemUiState.Error(e.canRetry)
        }
    }

    override fun onCleared() {
        Log.d("ItemViewModel", "onCleared for item $itemId")
    }
}
