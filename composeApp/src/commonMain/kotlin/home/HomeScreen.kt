package home

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import design.FitBodyLargeText
import design.FitBodyMediumText
import design.FitBodySmallText
import design.FitCard
import design.FitTopAppBar
import design.LocalNavAnimatedVisibilityScope
import design.LocalSharedTransitionScope
import home.components.FitCalendarPicker
import home.components.RoutinePicker
import home.model.Exercise
import home.presenter.CalendarState
import home.presenter.ExerciseEvent
import home.presenter.ExerciseState
import home.presenter.RoutinePickerState
import kotlinx.serialization.Serializable
import navigation.Route

@Serializable
object HomeRoute : Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    calendarState: CalendarState,
    routinePickerState: RoutinePickerState,
    exerciseState: ExerciseState,
    selectedDate: String,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            FitTopAppBar(
                title = selectedDate,
                scrollBehavior = scrollBehavior,
                actions = {
                    Icon(imageVector = Icons.Rounded.MoreVert, "More")
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                FitCalendarPicker(calendarState)
                RoutinePicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    routineState = routinePickerState,
                )
                LazyColumn(
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(exerciseState.exercises, key = { it.id }) { exercise ->
                        ExerciseItem(
                            modifier = Modifier,
                            exercise = exercise,
                            onExerciseEvent = exerciseState.onExerciseEvent,
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun ExerciseItem(
    exercise: Exercise,
    onExerciseEvent: (ExerciseEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    FitCard(
        modifier = modifier
            .padding(horizontal = 4.dp),
        onClick = {
            onExerciseEvent(ExerciseEvent.OnExerciseClicked(exercise))
        }
    ) {
        val sharedTransitionScope = LocalSharedTransitionScope.current
            ?: error("LocalSharedTransitionScope not provided")
        val animatedContentScope = LocalNavAnimatedVisibilityScope.current
            ?: error("LocalNavAnimatedVisibilityScope not provided")

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            with(sharedTransitionScope) {
                FitBodyLargeText(
                    text = exercise.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .sharedElement(
                            sharedTransitionScope.rememberSharedContentState(key = exercise.id),
                            animatedVisibilityScope = animatedContentScope
                        )
                )
            }
            if (exercise.reps != null && exercise.sets != null) {
                FitBodySmallText(
                    text = "${exercise.sets} X ${exercise.reps}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                )
            }
        }
    }
}

