package home.repository

import kotlinx.coroutines.flow.Flow

interface RoutineRepository {

    fun getRoutines(): Flow<List<Routine>>

    fun upsertRoutine(routine: Routine)
}

data class Routine()