package home.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import home.model.Routine
import home.presenter.RoutinePickerEvent.NavigateToRoutines
import home.presenter.RoutinePickerEvent.RoutineSelected
import home.repository.RoutineRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

sealed interface RoutinePickerEvent {
    data class RoutineSelected(val routine: RoutineInfo) : RoutinePickerEvent
    data object NavigateToRoutines : RoutinePickerEvent
}

fun Routine.toRoutineInfo(
    isSelected: Boolean = false
) = RoutineInfo(
    id = id,
    name = name,
    description = "$exercisesCount exercises",
    isSelected = isSelected,
    exerciseCount = exercisesCount,
)

data class RoutinePickerState(
    val routines: List<RoutineInfo> = emptyList(),
    val selectedRoutine: RoutineInfo? = null,
    val selectedDate: String = "",
    val onRoutineEvent: (RoutinePickerEvent) -> Unit = {},
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
        coroutineScope: CoroutineScope,
        selectedDate: String,
    ): StateFlow<RoutinePickerState> = coroutineScope.launchMolecule(RecompositionMode.Immediate) {
        RoutinePickerPresenter(
            selectedDate = selectedDate,
            routineRepository = routineRepository
        )
    }
}

@Composable
internal fun RoutinePickerPresenter(
    selectedDate: String,
    routineRepository: RoutineRepository,
): RoutinePickerState {
    val availableRoutines by routineRepository
        .observeRoutines()
        .map { routine -> routine.map(Routine::toRoutineInfo) }
        .collectAsState(emptyList())
    var selectedRoutine by remember { mutableStateOf<RoutineInfo?>(null) }

    return RoutinePickerState(
        routines = availableRoutines,
        selectedRoutine = selectedRoutine,
        selectedDate = selectedDate,
        onRoutineEvent = { event ->
            when (event) {
                is RoutineSelected -> {
                    selectedRoutine =
                        if (event.routine == selectedRoutine) null
                        else event.routine
                }

                is NavigateToRoutines -> {

                }
            }
        }
    )
}
