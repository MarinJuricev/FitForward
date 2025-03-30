package routine_creation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.ArcMode
import androidx.compose.animation.core.ExperimentalAnimationSpecApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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


@OptIn(
    ExperimentalSharedTransitionApi::class, ExperimentalAnimationSpecApi::class,
    ExperimentalMaterial3Api::class
)
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
                FitTopAppBar(
                    title = "Routine Creation"
                )
            }
        ) {
            FitCard(
                modifier = Modifier
                    .padding(it)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .sharedBounds(
                        sharedContentState = rememberSharedContentState(key = ROUTINE_KEY),
                        animatedVisibilityScope = animatedContentScope,
                        placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize,
                        resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                        boundsTransform = arcTransform(ArcMode.ArcBelow),
                    )
            ) {
                Column {
                    FitBodyMediumText(
                        modifier = Modifier.sharedElement(
                            sharedTransitionScope.rememberSharedContentState(key = ROUTINE_TEXT_KEY),
                            animatedVisibilityScope = animatedContentScope,
                            boundsTransform = arcTransform(ArcMode.ArcBelow),
                        ),
                        text = "Routines"
                    )

                    LazyColumn {
                        items(state.routines, key = { routine -> routine.id }) { routine ->
                            FitBodyMediumText(routine.name)
                        }
                    }
                }
            }
        }
    }
}
