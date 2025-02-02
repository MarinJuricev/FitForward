package home.repository

import home.model.Exercise
import home.model.Routine
import home.model.RoutineHistory
import kotlinx.coroutines.flow.Flow

interface RoutineRepository {

    fun observeRoutines(): Flow<List<Routine>>

    fun observeExercises(routineId: String): Flow<List<Exercise>>

    fun observeWorkoutHistoryByDate(date: String): Flow<List<Exercise>>

    suspend fun upsertRoutine(routine: Routine)

    suspend fun deleteRoutine(id: String)

    suspend fun upsertRoutineHistory(routineHistory: RoutineHistory)

    suspend fun deleteWorkoutExerciseByDate(date: String)

    fun observeRoutinesByDate(date: String): Flow<List<Routine>>
}

