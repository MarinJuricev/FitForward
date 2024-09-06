package home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
  Scaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    bottomBar = {
      var selectedItem by remember { mutableStateOf(bottomItems.first()) }

      BottomAppBar {
        bottomItems.forEach { bottomItem ->
          NavigationBarItem(
            selected = selectedItem == bottomItem,
            onClick = {
              selectedItem = bottomItem
            },
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
      MediumTopAppBar(
        title = { Text("Date") },
        scrollBehavior = scrollBehavior,
        actions = {
          Icon(imageVector = Icons.Rounded.MoreVert, "More")
        }
      )
    },
    content = { paddingValues ->
      Column(
        modifier = Modifier
          .padding(paddingValues)
          .fillMaxSize()
      ) {
        val pagerState = rememberPagerState(
          initialPage = 1,
          pageCount = { 3 }
        )

        HorizontalPager(
          state = pagerState,
          modifier = Modifier.fillMaxSize(),
        ) {
          when (it) {
            0 -> Text("1")
            1 -> Text("2")
            2 -> Text("3")
          }
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
