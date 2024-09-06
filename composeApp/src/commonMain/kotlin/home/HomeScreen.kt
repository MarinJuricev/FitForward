package home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.People
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen() {
  Scaffold(
    bottomBar = {
      BottomAppBar {
        bottomItems.forEach { bottomItem ->
          NavigationBarItem(
            selected = false,
            onClick = {},
            icon = {
              Icon(
                imageVector = bottomItem.image,
                contentDescription = bottomItem.contentDescription
              )
            }
          )
        }
      }
    },
    topBar = {
      TopAppBar(
        title = { Text("Date") },
        actions = {
          Icon(imageVector = Icons.Rounded.MoreVert, "More")
        }
      )
    },
    content = { paddingValues ->
      Column(
        modifier = Modifier.padding(paddingValues)
      ) {
        val pagerState = rememberPagerState(
          initialPage = 1,
          pageCount = { 3 }
        )

        HorizontalPager(pagerState) {
          Text("123")
        }
      }
    }
  )
}

private val bottomItems = listOf(
  BottomAppItem(image = Icons.Rounded.FitnessCenter, "Workout Screen"),
  BottomAppItem(image = Icons.Rounded.BarChart, "Statistics"),
  BottomAppItem(image = Icons.Rounded.People, "Login/Me"),
)

data class BottomAppItem(
  val image: ImageVector,
  val contentDescription: String,
)
