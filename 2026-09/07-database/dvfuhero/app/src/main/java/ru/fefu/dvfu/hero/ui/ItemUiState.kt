package ru.fefu.dvfu.hero.ui

import ru.fefu.dvfu.hero.model.Item
import ru.fefu.dvfu.hero.model.ItemDetails

sealed interface ItemUiState {
    data object Loading : ItemUiState
    data class Content(val item: Item, val details: ItemDetails) : ItemUiState
    data object NotFound : ItemUiState
    data class Error(val canRetry: Boolean) : ItemUiState
}
