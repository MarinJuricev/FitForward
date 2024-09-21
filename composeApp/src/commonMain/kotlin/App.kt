import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import design.FitForwardTheme
import home.HomeRoute
import home.HomeScreen

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
                HomeScreen()
            }
        }
    }
}