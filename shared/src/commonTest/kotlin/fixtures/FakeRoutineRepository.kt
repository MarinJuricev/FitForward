package fixtures

import home.model.Exercise
import home.model.Routine
import home.model.RoutineHistory
import home.repository.RoutineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRoutineRepository : RoutineRepository {
    override fun observeRoutines(): Flow<List<Routine>> =
        flowOf(listOf(Routine(id = "1", name = "Test Routine", exercisesCount = 3)))

    override fun observeExercises(routineId: String): Flow<List<Exercise>> =
        flowOf(listOf(Exercise(id = "ex1", name = "Push Up")))

    override fun observeWorkoutHistoryByDate(date: String): Flow<List<Exercise>> =
        flowOf(emptyList())

    override suspend fun upsertRoutine(routine: Routine) { /* no-op */
    }

    override suspend fun deleteRoutine(id: String) { /* no-op */
    }

    override suspend fun upsertRoutineHistory(routineHistory: RoutineHistory) { /* no-op */
    }

    override suspend fun deleteWorkoutExerciseByDate(date: String) { /* no-op */
    }

    override fun observeRoutinesByDate(date: String): Flow<List<Routine>> =
        flowOf(listOf(Routine(id = "1", name = "Test Routine", exercisesCount = 3)))
}