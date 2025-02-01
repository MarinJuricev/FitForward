package home.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import home.model.Exercise
import home.model.RoutineHistory
import home.repository.RoutineRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull

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
        routineId: String?,
        date: String,
    ): StateFlow<ExerciseState> = coroutineScope
        .launchMolecule(RecompositionMode.Immediate) {
            ExercisesByDatePresenter(
                routineId = routineId,
                selectedDate = date,
                routineRepository = routineRepository,
            )
        }
}

@Composable
internal fun ExercisesByDatePresenter(
    routineId: String?,
    selectedDate: String,
    routineRepository: RoutineRepository,
): ExerciseState {
    LaunchedEffect(selectedDate, routineId) {
        if (routineId == null) return@LaunchedEffect
        routineRepository.upsertRoutineHistory(
            RoutineHistory(
                routineId = routineId,
                performedAt = selectedDate,
                exercises = routineRepository
                    .observeExercises(routineId)
                    .firstOrNull()
                    .orEmpty(),
            )
        )
    }

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
