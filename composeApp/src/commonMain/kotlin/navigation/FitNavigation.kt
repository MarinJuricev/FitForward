package navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.People
import androidx.compose.ui.graphics.vector.ImageVector
import home.HomeRoute
import profile.ProfileScreen
import statistics.StatisticsScreen

val bottomItems = listOf(
    BottomAppItem(image = Icons.Rounded.FitnessCenter, HomeRoute, "Workout Screen"),
    BottomAppItem(image = Icons.Rounded.BarChart, StatisticsScreen, "Statistics"),
    BottomAppItem(image = Icons.Rounded.People, ProfileScreen, "Login/Me"),
)

data class BottomAppItem(
    val image: ImageVector,
    val route: Route,
    val contentDescription: String,
)

interface Route
