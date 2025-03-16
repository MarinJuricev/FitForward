package navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.spring
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.People
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import design.LocalNavAnimatedVisibilityScope
import home.HomeRoute
import profile.ProfileScreen
import statistics.StatisticsScreen
import kotlin.reflect.KType

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

// References from
// https://github.com/android/compose-samples/blob/3e509ff49d35b861fda8461e72469c23bb57085f/Jetsnack/app/src/main/java/com/example/jetsnack/ui/home/Home.kt#L88
public inline fun <reified T : Any> NavGraphBuilder.fitComposable(
    typeMap: Map<KType, NavType<*>> = emptyMap(),
    deepLinks: List<NavDeepLink> = emptyList(),
//    enterTransition: (
//    @JvmSuppressWildcards
//    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?
//    )? = {
//        fadeIn(nonSpatialExpressiveSpring())
//    },
//    exitTransition: (
//    @JvmSuppressWildcards
//    AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?
//    )? = {
//        fadeOut(nonSpatialExpressiveSpring())
//    },
//    popEnterTransition: (
//    @JvmSuppressWildcards
//    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?
//    )? =
//        enterTransition,
//    popExitTransition: (
//    @JvmSuppressWildcards
//    AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?
//    )? =
//        exitTransition,
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable<T>(
        typeMap = typeMap,
        deepLinks = deepLinks,
//        enterTransition,
//        exitTransition,
//        popEnterTransition,
//        popExitTransition
    ) {
        CompositionLocalProvider(
            LocalNavAnimatedVisibilityScope provides this@composable
        ) {
            content(it)
        }
    }
}

fun <T> spatialExpressiveSpring() = spring<T>(
    dampingRatio = 0.8f,
    stiffness = 380f
)

fun <T> nonSpatialExpressiveSpring() = spring<T>(
    dampingRatio = 1f,
    stiffness = 1600f
)

@OptIn(ExperimentalSharedTransitionApi::class)
val snackDetailBoundsTransform = BoundsTransform { _, _ ->
    spatialExpressiveSpring()
}