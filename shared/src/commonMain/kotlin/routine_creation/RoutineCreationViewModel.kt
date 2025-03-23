package routine_creation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import home.model.Routine
import home.repository.RoutineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import routine_creation.model.RoutineCreationState

class RoutineCreationViewModel(
    val routineRepository: RoutineRepository
) : ViewModel() {

    private val activeRoutines = MutableStateFlow<List<Routine>>(emptyList())
    private val isCreationActive = MutableStateFlow(false)

    val viewState = combine(
        activeRoutines,
        isCreationActive,
    ) { activeRoutines, isCreationActive ->
        RoutineCreationState(
            activeRoutines = activeRoutines,
            isCreateActive = isCreationActive,
            routineCreationEvent = {

            }
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, RoutineCreationState())


}