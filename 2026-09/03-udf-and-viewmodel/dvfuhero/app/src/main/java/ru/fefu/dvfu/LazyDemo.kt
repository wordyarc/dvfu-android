package ru.fefu.dvfu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


private data class RowItem(val id: Int, val title: String)

@Composable
private fun KeyDemo(useKeys: Boolean, items: List<RowItem>, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(if (useKeys) "WITH keys" else "NO keys", style = MaterialTheme.typography.titleMedium)

        LazyColumn(
            modifier = Modifier.height(300.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues(bottom = 8.dp)
        ) {
            if (useKeys) {
                items(items = items, key = { it.id }) { item ->
                    ToggleRow(item)
                }
            } else {
                items(items = items) { item ->
                    ToggleRow(item)
                }
            }
        }


    }
}

@Composable
private fun ToggleRow(item: RowItem) {
    // без keys у LazyColumn это состояние будет “переезжать” между строками при перестановке
    var checked by remember { mutableStateOf(false) }

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("${item.id}. ${item.title}")
        Checkbox(checked = checked, onCheckedChange = { checked = it })
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
private fun Preview_LazyKeys() {
    MaterialTheme {
        Surface {
            var list by remember {
                mutableStateOf(
                    listOf(
                        RowItem(1, "Alpha"),
                        RowItem(2, "Beta"),
                        RowItem(3, "Gamma"),
                        RowItem(4, "Delta"),
                        RowItem(5, "Epsilon"),
                    )
                )
            }

            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { list = list.shuffled() }) { Text("Shuffle") }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    KeyDemo(useKeys = false, items = list, modifier = Modifier.weight(1f))
                    KeyDemo(useKeys = true, items = list, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
