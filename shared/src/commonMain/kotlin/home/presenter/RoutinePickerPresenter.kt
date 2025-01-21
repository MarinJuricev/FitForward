package home.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import home.presenter.RoutinePickerEvent.NavigateToRoutines
import home.presenter.RoutinePickerEvent.RoutineSelected
import home.repository.Exercise
import home.repository.Routine
import home.repository.RoutineRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

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

fun RoutineInfo.toRoutine(
    isSelected: Boolean = false,
) = Routine(
    id = id,
    name = name,
    exercises = exercises,
    isSelected = isSelected,
)

data class RoutinePickerState(
    val routines: List<RoutineInfo>,
    val selectedRoutine: RoutineInfo? = null,
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
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): RoutinePickerState {
    var availableRoutines = produceState(emptyList<RoutineInfo>()) {
        routineRepository
            .observeRoutines()
            .map { routine -> routine.map(Routine::toRoutineInfo) }
            .collectLatest { value = it }
    }.value
    var selectedRoutine = remember {
        derivedStateOf { availableRoutines.find { it.isSelected } }
    }.value

    return RoutinePickerState(
        routines = availableRoutines,
        selectedRoutine = selectedRoutine,
        onRoutineEvent = { event ->
            when (event) {
                is RoutineSelected -> coroutineScope.launch {
                    routineRepository.upsertRoutine(event.routine.toRoutine(isSelected = true))
                }

                is NavigateToRoutines -> {

                }
            }
        }
    )
}
