package routine_creation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import design.FitBodyMediumText
import design.FitCard
import design.LocalNavAnimatedVisibilityScope
import design.LocalSharedTransitionScope
import home.components.ROUTINE_KEY
import home.components.ROUTINE_TEXT_KEY
import kotlinx.serialization.Serializable
import navigation.Route
import navigation.fitBoundsTransform

@Serializable
data class RoutineCreationRoute(
    val tag: String?,
) : Route


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RoutineCreationScreen(
    tag: String?
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: error("LocalSharedTransitionScope not provided")
    val animatedContentScope = LocalNavAnimatedVisibilityScope.current
        ?: error("LocalNavAnimatedVisibilityScope not provided")
    with(sharedTransitionScope) {
        Scaffold() {
            FitCard(
                modifier = Modifier
                    .padding(it)
                    .fillMaxWidth()
                    .renderInSharedTransitionScopeOverlay()
                    .sharedBounds(
                        rememberSharedContentState(key = ROUTINE_KEY),
                        animatedVisibilityScope = animatedContentScope,
                        placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize,
                        resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                        boundsTransform = fitBoundsTransform
                    ),
            ) {
                FitBodyMediumText(
                    modifier = Modifier.sharedElement(
                        sharedTransitionScope.rememberSharedContentState(key = ROUTINE_TEXT_KEY),
                        animatedVisibilityScope = animatedContentScope,
                        boundsTransform = fitBoundsTransform,
                    ),
                    text = "Routines"
                )
            }
        }
    }
}
