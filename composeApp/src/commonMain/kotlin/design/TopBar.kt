@file:OptIn(ExperimentalMaterial3Api::class)

package design

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FitTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    MediumTopAppBar(
        title = { FitTitleLargeText(text = title) },
        scrollBehavior = scrollBehavior,
        actions = actions,
//        navigationIcon = {
//            actions()
//            onNavigationClick?.let { clickHandler ->
//                IconButton(onClick = clickHandler) {
//                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
//                }
//            }
//        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier,
    )
}
