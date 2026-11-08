package ru.fefu.dvfu.hero.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.fefu.dvfu.hero.model.Item
import ru.fefu.dvfu.hero.model.ItemDetails

@Composable
fun ItemScreen(
    state: ItemUiState,
    canUse: Boolean,
    onRetry: () -> Unit,
    onUse: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        when (state) {
            ItemUiState.Loading -> LoadingBox()
            is ItemUiState.Content -> Details(state.item, state.details, canUse, onUse)
            ItemUiState.NotFound -> NotFoundBox()
            is ItemUiState.Error -> ErrorBox(state.canRetry, onRetry)
        }

        OutlinedButton(onClick = onBack) {
            Text("Назад")
        }
    }
}

@Composable
private fun LoadingBox() {
    CircularProgressIndicator()
    Text("Загружаем описание…")
}

@Composable
private fun Details(item: Item, details: ItemDetails, canUse: Boolean, onUse: () -> Unit) {
    Text(
        text = item.name,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
    )
    Text(
        text = "Восстанавливает ${item.heal} hp",
        style = MaterialTheme.typography.titleMedium,
    )
    Text(
        text = "${details.title} · ${details.rarity}",
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.secondary,
    )
    Text(
        text = details.description,
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
    )
    Button(onClick = onUse, enabled = canUse) {
        Text("Использовать")
    }
}

@Composable
private fun NotFoundBox() {
    Text(
        text = "Предмет не найден",
        style = MaterialTheme.typography.headlineSmall,
    )
}

@Composable
private fun ErrorBox(canRetry: Boolean, onRetry: () -> Unit) {
    Text(
        text = if (canRetry) "Не удалось загрузить описание" else "Не удалось прочитать ответ сервера",
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center,
    )
    if (canRetry) {
        Button(onClick = onRetry) {
            Text("Повторить")
        }
    }
}

private val previewItem = Item(7, "Зелье 8", heal = 20, apiIndex = "potion-of-healing-greater")

private val previewDetails = ItemDetails(
    title = "Potion of Greater Healing",
    rarity = "Uncommon",
    description = "You regain 4d4 + 4 hit points when you drink this potion. " +
        "The potion's red liquid glimmers when agitated.",
)

@Composable
private fun ItemScreenPreview(state: ItemUiState) {
    MaterialTheme {
        ItemScreen(state = state, canUse = true, onRetry = {}, onUse = {}, onBack = {})
    }
}

@Preview(showBackground = true, name = "Загрузка")
@Composable
fun ItemScreenLoadingPreview() = ItemScreenPreview(ItemUiState.Loading)

@Preview(showBackground = true, name = "Карточка предмета")
@Composable
fun ItemScreenContentPreview() = ItemScreenPreview(ItemUiState.Content(previewItem, previewDetails))

@Preview(showBackground = true, name = "Предмет не найден")
@Composable
fun ItemScreenNotFoundPreview() = ItemScreenPreview(ItemUiState.NotFound)

@Preview(showBackground = true, name = "Ошибка с повтором")
@Composable
fun ItemScreenErrorPreview() = ItemScreenPreview(ItemUiState.Error(canRetry = true))
