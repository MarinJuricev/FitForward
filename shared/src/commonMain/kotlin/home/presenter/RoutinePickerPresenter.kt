package home.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import home.repository.Exercise
import home.repository.Routine
import home.repository.RoutineRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map

sealed interface RoutinePickerEvent {
    data class RoutineSelected(val routine: RoutineInfo) : RoutinePickerEvent
    data object NavigateToRoutines : RoutinePickerEvent
}

fun Routine.toRoutineInfo(
    isSelected: Boolean = false,
) = RoutineInfo(
    id = id,
    name = name,
    exercises = exercises,
    description = "${exercises.count()} exercises",
    isSelected = isSelected,
)

data class RoutinePickerState(
    val routines: List<RoutineInfo>,
    val onRoutineEvent: (RoutinePickerEvent) -> Unit,
)

data class RoutineInfo(
    val id: String,
    val name: String,
    val isSelected: Boolean,
    val description: String,
    val exercises: List<Exercise>,
)

class RoutinePickerPresenterFactory(
    private val routineRepository: RoutineRepository,
) {

    fun create(
        coroutineScope: CoroutineScope
    ): StateFlow<RoutinePickerState> = coroutineScope.launchMolecule(RecompositionMode.Immediate) {
        RoutinePickerPresenter(
            routineRepository = routineRepository
        )
    }
}

@Composable
internal fun RoutinePickerPresenter(
    routineRepository: RoutineRepository,
): RoutinePickerState {
    var availableRoutines = produceState(emptyList<RoutineInfo>()) {
        routineRepository
            .observeRoutines()
            .map { routine -> routine.map(Routine::toRoutineInfo) }
            .collectLatest { value = it }
    }.value

    return RoutinePickerState(
        routines = availableRoutines,
        onRoutineEvent = {
            when (it) {
                is RoutinePickerEvent.RoutineSelected -> {

                }

                is RoutinePickerEvent.NavigateToRoutines -> {

                }
            }
        }
    )
}
