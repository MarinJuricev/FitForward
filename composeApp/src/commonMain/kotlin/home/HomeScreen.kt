@file:OptIn(
    ExperimentalFoundationApi::class, ExperimentalLayoutApi::class,
    ExperimentalUuidApi::class
)

package home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import home.components.WeekSelector
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.uuid.ExperimentalUuidApi

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
            val coroutineScope = rememberCoroutineScope()

            // Launch the molecule only once and remember the StateFlow
            val calendarStateFlow = remember {
                coroutineScope.launchMolecule(mode = RecompositionMode.ContextClock) {
                    CalendarPresenter(
                        dateProvider = {
                            Clock.System.now()
                                .toLocalDateTime(TimeZone.currentSystemDefault())
                                .date
                        }
                    )
                }
            }

            val calendarState by calendarStateFlow.collectAsState()
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                FitCalendarPicker(calendarState)
            }
        }
    )
}

@Composable
fun LazyAPIs(modifier: Modifier = Modifier) {
    var data by remember { mutableStateOf(List(10) { it.toString() }) }
    _root_ide_package_.androidx.compose.foundation.lazy.LazyColumn(
        modifier = modifier,
        horizontalAlignment = _root_ide_package_.androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        items(data, key = { it }) { index ->
            _root_ide_package_.androidx.compose.material3.Card(
                modifier = Modifier
                    .background(_root_ide_package_.androidx.compose.material3.MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = index,
                    textAlign = _root_ide_package_.androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        item {
            _root_ide_package_.androidx.compose.material3.Button(
                onClick = { data = data.shuffled() }) {
                Text("Randomize the list")
            }
        }
        item {
            _root_ide_package_.androidx.compose.material3.Button(
                onClick = {
                    data = data.plus(_root_ide_package_.kotlin.uuid.Uuid.random().toString())
                }) {
                Text("Add ann Item")
            }
        }
        item {
            _root_ide_package_.androidx.compose.material3.Button(
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
    var maxLines by remember { _root_ide_package_.androidx.compose.runtime.mutableIntStateOf(1) }
    _root_ide_package_.androidx.compose.foundation.layout.ContextualFlowRow(
        modifier = modifier.fillMaxSize(),
        maxLines = maxLines,
        verticalArrangement = _root_ide_package_.androidx.compose.foundation.layout.Arrangement.Center,
        overflow = _root_ide_package_.androidx.compose.foundation.layout.ContextualFlowRowOverflow.expandIndicator {
            val remainingItems = totalItemCount - shownItemCount
            _root_ide_package_.androidx.compose.material3.Button(
                modifier = Modifier.align(_root_ide_package_.androidx.compose.ui.Alignment.CenterVertically),
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

@Composable
fun FitCalendarPicker(
    state: CalendarState,
    modifier: Modifier = Modifier
) {

    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { 3 }
    )

    HorizontalPager(
        modifier = modifier.fillMaxWidth(),
        state = pagerState,
    ) { pageIndex ->
        val startIndex = pageIndex * 7
        val endIndex = minOf(startIndex + 7, state.days.size)
        val daysForPage = state.days.subList(startIndex, endIndex)
        WeekSelector(
            days = daysForPage,
            onDayClick = state.onDayClick
        )
    }

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
