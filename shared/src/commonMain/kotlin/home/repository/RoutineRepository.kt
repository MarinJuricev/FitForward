package home.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.fitforward.data.FitForwardDatabase
import core.AppCoroutineDispatchers
import core.IdProvider
import home.model.Exercise
import home.model.Routine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface RoutineRepository {

    fun observeRoutines(): Flow<List<Routine>>

    fun observeExercises(routineId: String): Flow<List<Exercise>>

    suspend fun upsertRoutine(routine: Routine)

    suspend fun deleteRoutine(id: String)

    suspend fun insertRoutineHistory(
        routineId: String,
        date: String,
    )

    fun observeRoutinesByDate(date: String): Flow<List<Routine>>
}

class SqlDelightRoutineRepository(
    fitForwardDatabase: FitForwardDatabase,
    private val idProvider: IdProvider,
    private val coroutineDispatchers: AppCoroutineDispatchers,
) : RoutineRepository {

    private val routineQueries = fitForwardDatabase.routineQueries
    private val exerciseQueries = fitForwardDatabase.routineExerciseQueries
    private val queries = fitForwardDatabase.routineExerciseQueries
    private val historyQueries = fitForwardDatabase.routineHistoryQueries

    override fun observeRoutines(): Flow<List<Routine>> = routineQueries
        .selectAllRoutines()
        .asFlow()
        .mapToList(coroutineDispatchers.io)
        .map { dbRoutineList ->
            dbRoutineList.map { dbRoutine ->
                Routine(
                    id = dbRoutine.routineId,
                    name = dbRoutine.routineName,
                    exercisesCount = exerciseQueries.selectExercisesForRoutine(dbRoutine.routineId)
                        .executeAsList()
                        .count()
                )
            }
        }

    override fun observeExercises(
        routineId: String
    ): Flow<List<Exercise>> = exerciseQueries.selectExercisesForRoutine(routineId)
        .asFlow()
        .mapToList(coroutineDispatchers.io)
        .map { dbExercises -> dbExercises.map { Exercise(it.exerciseId, it.exerciseName) } }

    override suspend fun upsertRoutine(routine: Routine) {
        withContext(coroutineDispatchers.io) {
            val existing = routineQueries.selectRoutineById(routine.id).executeAsOneOrNull()
            if (existing == null) {
                routineQueries.insertRoutine(
                    id = routine.id,
                    name = routine.name,
                )
            } else {
                routineQueries.updateRoutine(
                    id = routine.id,
                    name = routine.name,
                )
            }
        }
    }

    override suspend fun deleteRoutine(id: String) {
        withContext(coroutineDispatchers.io) {
            routineQueries.deleteRoutine(id)
        }
    }

    override suspend fun insertRoutineHistory(
        routineId: String,
        date: String,
    ) {
        withContext(coroutineDispatchers.io) {
            historyQueries.insertRoutineHistory(
                id = idProvider.generate(),
                routineId = routineId,
                performedAt = date,
                durationSeconds = null,
                notes = null,
            )
        }
    }

    override fun observeRoutinesByDate(
        date: String,
    ): Flow<List<Routine>> = historyQueries
        .selectRoutinesByDate(date)
        .asFlow()
        .mapToList(coroutineDispatchers.io)
        .map { dbRoutines -> dbRoutines.map { Routine(it.routineId, it.routineName, 0) } }
}
