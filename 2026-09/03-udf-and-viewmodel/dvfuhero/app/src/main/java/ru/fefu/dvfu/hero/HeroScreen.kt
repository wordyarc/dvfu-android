package ru.fefu.dvfu.hero

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HeroScreen(
    state: HeroUiState,
    onDamage: () -> Unit,
    onHeal: () -> Unit,
    onReset: () -> Unit,
    onUseItem: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HeroHeader(name = state.name, isDefeated = state.isDefeated)

        Spacer(Modifier.height(16.dp))

        HealthPanel(hp = state.hp, maxHp = state.maxHp)

        Spacer(Modifier.height(16.dp))

        HeroActions(
            isDefeated = state.isDefeated,
            isFullHp = state.isFullHp,
            onDamage = onDamage,
            onHeal = onHeal,
            onReset = onReset,
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Инвентарь: ${state.inventory.size}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth(),
        )

        if (state.inventory.isEmpty()) {
            Text(
                text = "Инвентарь пуст",
                modifier = Modifier.padding(16.dp),
            )
        } else {
            Inventory(
                inventory = state.inventory,
                enabled = !state.isDefeated,
                onUse = onUseItem,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            )
        }
    }
}

@Composable
fun HeroHeader(name: String, isDefeated: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Image(
                painter = painterResource(R.drawable.knight),
                contentDescription = "Рыцарь",
                modifier = Modifier.size(120.dp),
                colorFilter = if (isDefeated) grayscale else null,
                alpha = if (isDefeated) 0.5f else 1f,
            )
        }

        Spacer(Modifier.width(16.dp))

        Text(
            text = name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun HealthPanel(hp: Int, maxHp: Int) {
    Column(Modifier.fillMaxWidth()) {
        LinearProgressIndicator(
            progress = { hp.toFloat() / maxHp },
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp),
            color = hpColor(hp, maxHp),
        )

        Spacer(Modifier.height(4.dp))

        Row(Modifier.fillMaxWidth()) {
            Text(
                text = statusText(hp, maxHp),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "$hp / $maxHp HP",
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
fun HeroActions(
    isDefeated: Boolean,
    isFullHp: Boolean,
    onDamage: () -> Unit,
    onHeal: () -> Unit,
    onReset: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = onDamage,
                enabled = !isDefeated,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            ) {
                Text("Получить урон")
            }
            Button(
                onClick = onHeal,
                enabled = !isDefeated && !isFullHp,
            ) {
                Text("Лечение")
            }
        }

        if (isDefeated) {
            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick = onReset) {
                Text("Начать заново")
            }
        }
    }
}

@Composable
fun Inventory(
    inventory: List<Item>,
    enabled: Boolean,
    onUse: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier) {
        items(inventory, key = { it.id }) { item ->
            ItemRow(
                item = item,
                enabled = enabled,
                onClick = { onUse(item.id) },
            )
        }
    }
}

@Composable
fun ItemRow(item: Item, enabled: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onClick)
            .alpha(if (enabled) 1f else 0.4f)
            .padding(16.dp)
    ) {
        Text(item.name, modifier = Modifier.weight(1f))
        Text("+${item.heal} hp")
    }
}

private val grayscale = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })

fun statusText(hp: Int, maxHp: Int): String = when {
    hp == 0 -> "💀 Пал в бою"
    hp < maxHp * 0.3 -> "🩸 Тяжело ранен"
    hp < maxHp -> "⚔️ Сражается"
    else -> "🛡️ Готов к бою"
}

fun hpColor(hp: Int, maxHp: Int): Color = when {
    hp < maxHp * 0.3 -> Color(0xFFC62828)
    hp < maxHp * 0.6 -> Color(0xFFF9A825)
    else -> Color(0xFF2E7D32)
}

@Preview(showBackground = true, name = "Полное здоровье")
@Composable
fun HeroScreenFullPreview() {
    MaterialTheme {
        HeroScreen(
            state = HeroUiState(inventory = startItems()),
            onDamage = {},
            onHeal = {},
            onReset = {},
            onUseItem = {},
        )
    }
}

@Preview(showBackground = true, name = "Персонаж побеждён")
@Composable
fun HeroScreenDefeatedPreview() {
    MaterialTheme {
        HeroScreen(
            state = HeroUiState(hp = 0, inventory = startItems().take(5)),
            onDamage = {},
            onHeal = {},
            onReset = {},
            onUseItem = {},
        )
    }
}

@Preview(showBackground = true, name = "Инвентарь пуст")
@Composable
fun HeroScreenEmptyInventoryPreview() {
    MaterialTheme {
        HeroScreen(
            state = HeroUiState(hp = 40),
            onDamage = {},
            onHeal = {},
            onReset = {},
            onUseItem = {},
        )
    }
}
