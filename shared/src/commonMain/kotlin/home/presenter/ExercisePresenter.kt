package home.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import home.repository.Exercise
import home.repository.RoutineRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

sealed interface ExerciseEvent {
    data class OnExerciseClicked(val exercise: Exercise) : ExerciseEvent
}

data class ExerciseState(
    val exercises: List<Exercise>,
    val onExerciseEvent: (ExerciseEvent) -> Unit,
)

class ExercisePresenterFactory(
    private val routineRepository: RoutineRepository,
) {

    fun create(
        coroutineScope: CoroutineScope
    ): StateFlow<ExerciseState> = coroutineScope
        .launchMolecule(RecompositionMode.Immediate) {
            ExercisePresenter(
                routineRepository = routineRepository,
            )
        }
}

@Composable
internal fun ExercisePresenter(
    routineRepository: RoutineRepository,
): ExerciseState {
    val exercises by routineRepository.observeExercises().collectAsState(emptyList())
//    var exercises = produceState(emptyList<Exercise>()) {
//        routineRepository
//            .observeExercises()
//            .collectLatest { value = it }
//    }.value

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
