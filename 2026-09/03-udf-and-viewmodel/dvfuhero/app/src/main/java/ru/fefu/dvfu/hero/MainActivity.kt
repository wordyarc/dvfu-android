package ru.fefu.dvfu.hero

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel: HeroViewModel = viewModel()

                    HeroScreen(
                        state = viewModel.uiState,
                        onDamage = viewModel::onDamage,
                        onHeal = viewModel::onHeal,
                        onReset = viewModel::onReset,
                        onUseItem = viewModel::onUseItem,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}
