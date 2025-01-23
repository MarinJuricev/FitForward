package home.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import home.presenter.RoutinePickerEvent.NavigateToRoutines
import home.presenter.RoutinePickerEvent.RoutineSelected
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

fun Routine.toRoutineInfo() = RoutineInfo(
    id = id,
    name = name,
    description = "$exercisesCount exercises",
    isSelected = isSelected,
    exerciseCount = exercisesCount,
)

fun RoutineInfo.toRoutine(
    isSelected: Boolean = false,
) = Routine(
    id = id,
    name = name,
    exercisesCount = exerciseCount,
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
    val exerciseCount: Int,
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
    val availableRoutines by routineRepository
        .observeRoutines()
        .map { routine -> routine.map(Routine::toRoutineInfo) }
        .collectAsState(emptyList())

    val selectedRoutine = remember(availableRoutines) {
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
