package home.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

interface RoutineRepository {

    fun observeRoutines(): Flow<List<Routine>>

    fun upsertRoutine(routine: Routine)

    fun deleteRoutine(id: String)
}

class InMemoryRoutineRepository : RoutineRepository {

    private val routines = MutableStateFlow<List<Routine>>(emptyList())

    override fun observeRoutines(): Flow<List<Routine>> =
        routines

    override fun upsertRoutine(routine: Routine) {
        routines.update { it.plus(routine) }
    }

    override fun deleteRoutine(id: String) {
        routines.update { it.filter { routine -> routine.id != id } }
    }
}

data class Routine(
    val id: String,
    val name: String,
    val description: String,
)