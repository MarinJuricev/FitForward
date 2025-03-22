@file:OptIn(ExperimentalSharedTransitionApi::class)

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import design.FitForwardTheme
import exercisedetail.ExerciseDetailRoute
import exercisedetail.ExerciseDetailViewModel
import exercisedetail.ExerciseDetailsScreen
import home.HomeRoute
import home.HomeScreen
import home.HomeViewModel
import home.presenter.ExerciseEffect
import navigation.bottomItems
import navigation.fitComposable
import org.koin.compose.viewmodel.koinViewModel
import profile.ProfileScreen
import statistics.StatisticsScreen

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
                fitComposable<HomeRoute> {
                    val homeViewModel = koinViewModel<HomeViewModel>()

                    val calendarState by homeViewModel.calendarState.collectAsState()
                    val selectedDate by homeViewModel.selectedDate.collectAsState()
                    val routinePickerState by homeViewModel.routinePickerState.collectAsState()
                    val exerciseState by homeViewModel.exerciseState.collectAsState()

                    LaunchedEffect(exerciseState.viewEffect) {
                        exerciseState.viewEffect.collect {
                            when (it) {
                                is ExerciseEffect.OnExerciseClicked -> navController.navigate(
                                    ExerciseDetailRoute(it.exercise.id, "Pull")
                                )
                            }
                        }
                    }

                    HomeScreen(
                        selectedDate = selectedDate,
                        calendarState = calendarState,
                        routinePickerState = routinePickerState,
                        exerciseState = exerciseState,
                    )
                }

                fitComposable<ExerciseDetailRoute> {
                    val exerciseDetailViewModel = koinViewModel<ExerciseDetailViewModel>()
                    val test by exerciseDetailViewModel.exercise.collectAsState()

                    ExerciseDetailsScreen(
                        exerciseId = test,
                        exerciseName = "Pull-up",
                    )
                }

                fitComposable<StatisticsScreen> {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "WIP Statistics"
                        )
                    }
                }

                fitComposable<ProfileScreen> {
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

