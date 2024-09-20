@file:OptIn(ExperimentalUuidApi::class, ExperimentalLayoutApi::class)

package home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ContextualFlowRow
import androidx.compose.foundation.layout.ContextualFlowRowOverflow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun LazyAPIs(modifier: Modifier = Modifier) {
    var data by remember { mutableStateOf(List(10) { it.toString() }) }
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(data, key = { it }) { index ->
            Card(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = index,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        item {
            Button(
                onClick = { data = data.shuffled() }) {
                Text("Randomize the list")
            }
        }
        item {
            Button(
                onClick = { data = data.plus(Uuid.random().toString()) }) {
                Text("Add ann Item")
            }
        }
        item {
            Button(
                onClick = { data = data.drop(1) }) {
                Text("Remove an Item")
            }
        }
    }
}

@Composable
private fun OverflowLayout(
    items: List<String> = List(20) { it.toString() },
    modifier: Modifier = Modifier
) {
    var maxLines by remember { mutableIntStateOf(1) }
    ContextualFlowRow(
        modifier = modifier.fillMaxSize(),
        maxLines = maxLines,
        verticalArrangement = Arrangement.Center,
        overflow = ContextualFlowRowOverflow.expandIndicator {
            val remainingItems = totalItemCount - shownItemCount
            Button(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = { maxLines += 1 },
            ) {
                Text("Show $remainingItems more")
            }
        },
        itemCount = items.size
    ) { index ->
        Text(
            modifier = Modifier.padding(8.dp),
            text = index.toString(),
            fontSize = 64.sp
        )
    }
}