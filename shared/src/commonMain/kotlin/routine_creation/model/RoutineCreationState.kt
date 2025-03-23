package routine_creation.model

import home.model.Routine

data class RoutineCreationState(
    val activeRoutines: List<Routine> = emptyList(),
    val isCreateActive: Boolean = false,
    val routineCreationEvent: (RoutineCreationEvent) -> Unit = {},
)
