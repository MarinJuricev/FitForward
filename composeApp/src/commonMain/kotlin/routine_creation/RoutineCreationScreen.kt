package routine_creation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.ArcMode
import androidx.compose.animation.core.ExperimentalAnimationSpecApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import design.FitBodyMediumText
import design.FitCard
import design.FitTopAppBar
import design.LocalNavAnimatedVisibilityScope
import design.LocalSharedTransitionScope
import home.components.ROUTINE_KEY
import home.components.ROUTINE_TEXT_KEY
import kotlinx.serialization.Serializable
import navigation.Route
import navigation.arcTransform
import routine_creation.model.RoutineCreationState

@Serializable
data class RoutineCreationRoute(
    val tag: String?,
) : Route


@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalAnimationSpecApi::class)
@Composable
fun RoutineCreationScreen(
    state: RoutineCreationState,
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: error("LocalSharedTransitionScope not provided")
    val animatedContentScope = LocalNavAnimatedVisibilityScope.current
        ?: error("LocalNavAnimatedVisibilityScope not provided")

    with(sharedTransitionScope) {
        Scaffold(
           topBar = {
//              FitTopAppBar(
//
//              )
           }
        ) {
            FitCard(
                modifier = Modifier
                    .padding(it)
                    .fillMaxWidth()
                    .sharedBounds(
                        sharedContentState = rememberSharedContentState(key = ROUTINE_KEY),
                        animatedVisibilityScope = animatedContentScope,
                        placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize,
                        resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                        boundsTransform = arcTransform(ArcMode.ArcBelow),
                    ),
            ) {
                FitBodyMediumText(
                    modifier = Modifier.sharedElement(
                        sharedTransitionScope.rememberSharedContentState(key = ROUTINE_TEXT_KEY),
                        animatedVisibilityScope = animatedContentScope,
                        boundsTransform = arcTransform(ArcMode.ArcBelow),
                    ),
                    text = "Routines"
                )
            }
        }
    }
}
