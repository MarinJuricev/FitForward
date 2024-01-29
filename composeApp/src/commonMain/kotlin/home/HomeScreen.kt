package home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen() {
    Scaffold(
        bottomBar = {
            BottomAppBar {
                repeat(3) {
                    NavigationBarItem(
                        selected = false,
                        onClick = {},
                        icon = { Text("Hello") }
                    )
                }
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues)
            ) {
                Text("Hello World!")
            }
        }
    )
}