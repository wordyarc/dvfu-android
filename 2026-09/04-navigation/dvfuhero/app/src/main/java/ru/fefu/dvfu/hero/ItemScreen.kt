package ru.fefu.dvfu.hero

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ItemScreen(
    state: ItemUiState,
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
        val item = state.item
        if (item == null) {
            Text(
                text = "Предмет не найден",
                style = MaterialTheme.typography.headlineSmall,
            )
        } else {
            Text(
                text = item.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Восстанавливает ${item.heal} hp",
                style = MaterialTheme.typography.titleMedium,
            )
            Button(onClick = onUse, enabled = state.canUse) {
                Text("Использовать")
            }
        }

        OutlinedButton(onClick = onBack) {
            Text("Назад")
        }
    }
}

@Preview(showBackground = true, name = "Карточка предмета")
@Composable
fun ItemScreenPreview() {
    MaterialTheme {
        ItemScreen(
            state = ItemUiState(item = Item(7, "Зелье 8", heal = 20), canUse = true),
            onUse = {},
            onBack = {},
        )
    }
}

@Preview(showBackground = true, name = "Предмет не найден")
@Composable
fun ItemScreenNotFoundPreview() {
    MaterialTheme {
        ItemScreen(
            state = ItemUiState(item = null, canUse = false),
            onUse = {},
            onBack = {},
        )
    }
}
