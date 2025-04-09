package routine_creation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import home.model.Routine
import home.repository.RoutineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import routine_creation.model.RoutineCreationState

class RoutineCreationViewModel(
    private val routineRepository: RoutineRepository
) : ViewModel() {

    private val activeRoutines = MutableStateFlow<List<Routine>>(emptyList())
    private val isCreationActive = MutableStateFlow(false)

    val viewState = combine(
        activeRoutines,
        isCreationActive,
    ) { activeRoutines, isCreationActive ->
        RoutineCreationState(
            routines = activeRoutines,
            isCreateActive = isCreationActive,
            routineCreationEvent = {

            }
        )
    }.onStart {
        observeRoutines()
    }.stateIn(viewModelScope, SharingStarted.Lazily, RoutineCreationState())

    private fun observeRoutines() {
        routineRepository
            .observeRoutines()
            .onEach { routines ->
                activeRoutines.update { routines }
            }.launchIn(viewModelScope)
    }
}