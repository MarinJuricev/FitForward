package home.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

data class RoutinePickerState(
    val routines: List<RoutineInfo>,
    val onRoutineClick: (RoutineInfo) -> Unit,
)

data class RoutineInfo(
    val id: String,
    val name: String,
    val description: String,
    val isSelected: Boolean,
)

class RoutinePickerPresenterFactory {

    fun create(
        coroutineScope: CoroutineScope
    ): StateFlow<CalendarState> = coroutineScope
        .launchMolecule(RecompositionMode.Immediate) {
            RoutinePickerPresenter()
        }
}

@Composable
internal fun RoutinePickerPresenter(): RoutinePickerState {
    var availableRoutines by remember {
        mutableStateOf(generateDataSet())
    }

    return RoutinePickerState(
        routines = availableRoutines,
        onRoutineClick = { selectedRoutine ->
            availableRoutines = availableRoutines.map { routine ->
                routine.copy(isSelected = routine == selectedRoutine)
            }
        },
    )
}