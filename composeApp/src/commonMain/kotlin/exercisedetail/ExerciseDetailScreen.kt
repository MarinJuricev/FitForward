package exercisedetail

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import design.FitBodyMediumText
import design.LocalNavAnimatedVisibilityScope
import design.LocalSharedTransitionScope
import kotlinx.serialization.Serializable
import navigation.Route

@Serializable
data class ExerciseDetailRoute(
    val exerciseId: String,
    val exerciseName: String,
) : Route

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ExerciseDetailsScreen(
    exerciseId: String,
    exerciseName: String,
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: error("LocalSharedTransitionScope not provided")
    val animatedContentScope = LocalNavAnimatedVisibilityScope.current
        ?: error("LocalNavAnimatedVisibilityScope not provided")
    with(sharedTransitionScope) {
        FitBodyMediumText(
            text = exerciseName,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .sharedElement(
                    sharedTransitionScope.rememberSharedContentState(key = exerciseId),
                    animatedVisibilityScope = animatedContentScope
                )
        )
    }
    Text("Exercise Details")
}