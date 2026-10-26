package ru.fefu.dvfu.hero

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable

@Serializable
data object HeroKey : NavKey

@Serializable
data class ItemKey(val itemId: Int) : NavKey

@Composable
fun HeroNavigation(repo: GameRepository, modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(HeroKey)

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<HeroKey> {
                val viewModel = viewModel { HeroViewModel(repo) }
                HeroScreen(
                    state = viewModel.uiState,
                    onDamage = viewModel::onDamage,
                    onHeal = viewModel::onHeal,
                    onReset = viewModel::onReset,
                    onOpenItem = { id ->
                        if (backStack.lastOrNull() == HeroKey) backStack.add(ItemKey(id))
                    },
                )
            }
            entry<ItemKey> { key ->
                val viewModel = viewModel { ItemViewModel(key.itemId, repo) }
                val close: () -> Unit = {
                    if (backStack.lastOrNull() == key) backStack.removeLastOrNull()
                }
                ItemScreen(
                    state = viewModel.uiState,
                    onUse = {
                        viewModel.onUse()
                        close()
                    },
                    onBack = close,
                )
            }
        },
    )
}
