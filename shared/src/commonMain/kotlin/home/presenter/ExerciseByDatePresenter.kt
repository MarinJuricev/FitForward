package home.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import home.model.Exercise
import home.repository.RoutineRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

sealed interface ExerciseEvent {
    data class OnExerciseClicked(val exercise: Exercise) : ExerciseEvent
}

sealed interface ExerciseEffect {
    data class OnExerciseClicked(val exercise: Exercise) : ExerciseEffect
}

data class ExerciseState(
    val exercises: List<Exercise> = emptyList(),
    val viewEffect: Flow<ExerciseEffect> = emptyFlow(),
    val onExerciseEvent: (ExerciseEvent) -> Unit = {},
)

class ExerciseByDatePresenterFactory(
    private val routineRepository: RoutineRepository,
) {

    fun create(
        coroutineScope: CoroutineScope,
        date: String,
    ): StateFlow<ExerciseState> = coroutineScope
        .launchMolecule(RecompositionMode.Immediate) {
            ExercisesByDatePresenter(
                coroutineScope = coroutineScope,
                selectedDate = date,
                routineRepository = routineRepository,
            )
        }
}

@Composable
internal fun ExercisesByDatePresenter(
    coroutineScope: CoroutineScope,
    selectedDate: String,
    routineRepository: RoutineRepository,
): ExerciseState {
    val viewEffect = remember { Channel<ExerciseEffect>(Channel.BUFFERED) }
    val exercises by routineRepository
        .observeWorkoutHistoryByDate(selectedDate)
        .collectAsState(emptyList())

    return ExerciseState(
        exercises = exercises,
        viewEffect = viewEffect.receiveAsFlow(),
        onExerciseEvent = { event ->
            when (event) {
                is ExerciseEvent.OnExerciseClicked -> coroutineScope.launch {
                    viewEffect.send(ExerciseEffect.OnExerciseClicked(event.exercise))
                }
            }
        }
    )
}
