import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import design.FitForwardTheme
import home.HomeRoute
import home.HomeScreen
import home.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(
    navController: NavHostController = rememberNavController()
) {
    FitForwardTheme {
        NavHost(
            navController = navController,
            startDestination = HomeRoute
        ) {
            composable<HomeRoute> {
                val homeViewModel = koinViewModel<HomeViewModel>()
                val calendarState by homeViewModel.calendarState.collectAsState()

                HomeScreen(
                    calendarState = calendarState,
                )
            }
        }
    }
}