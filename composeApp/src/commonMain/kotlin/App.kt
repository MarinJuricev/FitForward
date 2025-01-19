import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.People
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import design.FitForwardTheme
import home.HomeRoute
import home.HomeScreen
import home.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel
import profile.ProfileRoute
import statistics.StatisticsRoute

@Composable
fun App(
    navController: NavHostController = rememberNavController()
) {
    FitForwardTheme {
        Scaffold(
            bottomBar = {
                BottomAppBar {
                    var selectedItem by remember { mutableStateOf(bottomItems.first()) }

                    bottomItems.forEach { bottomItem ->
                        NavigationBarItem(
                            selected = selectedItem == bottomItem,
                            onClick = {
                                selectedItem = bottomItem
                                navController.navigate(bottomItem.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
                                    popUpTo(navController.graph.findStartDestination().route.orEmpty()) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
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
        ) { paddingValues ->
            NavHost(
                modifier = Modifier.padding(paddingValues),
                navController = navController,
                startDestination = HomeRoute
            ) {
                composable<HomeRoute> {
                    val homeViewModel = koinViewModel<HomeViewModel>()

                    val calendarState by homeViewModel.calendarState.collectAsState()
                    val selectedDate by homeViewModel.selectedDate.collectAsState()
                    val routinePickerState by homeViewModel.routinePickerState.collectAsState()

                    HomeScreen(
                        selectedDate = selectedDate,
                        calendarState = calendarState,
                        routinePickerState = routinePickerState,
                    )
                }

                composable<StatisticsRoute> {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "WIP Statistics"
                        )
                    }
                }

                composable<ProfileRoute> {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "WIP ProfileRoute"
                        )
                    }
                }
            }
        }
    }
}

private val bottomItems = listOf(
    BottomAppItem(image = Icons.Rounded.FitnessCenter, HomeRoute, "Workout Screen"),
    BottomAppItem(image = Icons.Rounded.BarChart, StatisticsRoute, "Statistics"),
    BottomAppItem(image = Icons.Rounded.People, ProfileRoute, "Login/Me"),
)

data class BottomAppItem(
    val image: ImageVector,
    val route: Route,
    val contentDescription: String,
)

interface Route
