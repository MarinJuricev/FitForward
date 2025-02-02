package home.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import home.model.Exercise
import home.repository.RoutineRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

sealed interface ExerciseEvent {
    data class OnExerciseClicked(val exercise: Exercise) : ExerciseEvent
}

data class ExerciseState(
    val exercises: List<Exercise> = emptyList(),
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
                selectedDate = date,
                routineRepository = routineRepository,
            )
        }
}

@Composable
internal fun ExercisesByDatePresenter(
    selectedDate: String,
    routineRepository: RoutineRepository,
): ExerciseState {
    val exercises by routineRepository
        .observeWorkoutHistoryByDate(selectedDate)
        .collectAsState(emptyList())

    return ExerciseState(
        exercises = exercises,
        onExerciseEvent = { event ->
            when (event) {
                is ExerciseEvent.OnExerciseClicked -> {

                }
            }
        }
    )

}
