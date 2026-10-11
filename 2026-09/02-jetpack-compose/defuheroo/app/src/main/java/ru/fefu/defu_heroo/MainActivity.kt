package ru.fefu.defu_heroo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val superHero = remember { HeroState("Steve", 100) }

                    HeroCard(
                        hero = superHero,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


data class KnightState(
    val hp: Int = 1000,
    val name: String = ""
)
class HeroState(val name: String, val maxHp: Int = 100) {

    var hp by mutableIntStateOf(maxHp)
        private set

//    var hp = maxHp

    val isAlive: Boolean
        get() = hp > 0

    val isFullHp: Boolean
        get() = hp == maxHp

    fun takeDamage(amount: Int = 20) {
        Log.i("======================", "$hp")
        hp = (hp - amount).coerceAtLeast(0)
    }

    fun heal(amount: Int = 15) {
        hp = (hp + amount).coerceAtMost(maxHp)
    }

    fun restart() {
        hp = maxHp
    }
}


@Composable
fun HeroCard(hero: HeroState, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Card(colors = CardDefaults.cardColors(contentColor = Color.White)) {
            Image(
                painter = painterResource(R.drawable.knight),
                contentDescription = "Рыцарь",
                modifier = Modifier.size(240.dp),
                colorFilter = if (hero.isAlive) null else ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) }),
                alpha = if (hero.isAlive) 1f else 0.5f
             )
        }


        Spacer(Modifier.height(16.dp))

        Text(
            text = hero.name,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        LinearProgressIndicator(
            progress = { hero.hp.toFloat() / hero.maxHp },
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp),
            color = hpColor(hero.hp, hero.maxHp)
        )

        Text(
            text = "${hero.hp} / ${hero.maxHp} HP",
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = statusText(hero.hp, hero.maxHp),
        )

        Spacer(Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = { hero.takeDamage() },
                enabled = hero.isAlive,
            ) {
                Text("Получить урон")
            }
            Button(
                onClick = { hero.heal() },
                enabled = hero.isAlive && !hero.isFullHp,
            ) {
                Text("Лечение")
            }
        }

        if (!hero.isAlive) {
            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick = { hero.restart() }) {
                Text("Начать заново")
            }
        }
    }
}







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