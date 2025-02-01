package home.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.fitforward.data.FitForwardDatabase
import core.AppCoroutineDispatchers
import core.IdProvider
import home.model.Exercise
import home.model.Routine
import home.model.RoutineHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface RoutineRepository {

    fun observeRoutines(): Flow<List<Routine>>

    fun observeExercises(routineId: String): Flow<List<Exercise>>

    fun observeExercisesByDate(
        routineId: String,
        date: String
    ): Flow<List<Exercise>>

    suspend fun upsertRoutine(routine: Routine)

    suspend fun deleteRoutine(id: String)

    suspend fun insertRoutineHistory(routineHistory: RoutineHistory)

    fun observeRoutinesByDate(date: String): Flow<List<Routine>>
}

class SqlDelightRoutineRepository(
    private val fitForwardDatabase: FitForwardDatabase,
    private val idProvider: IdProvider,
    private val coroutineDispatchers: AppCoroutineDispatchers,
) : RoutineRepository {

    private val routineQueries = fitForwardDatabase.routineQueries
    private val routineTemplates = fitForwardDatabase.routineTemplateQueries
    private val historyQueries = fitForwardDatabase.workoutHistoryQueries
    private val workoutExercises = fitForwardDatabase.workoutExerciseQueries

    override fun observeRoutines(): Flow<List<Routine>> = routineQueries
        .selectAllRoutines()
        .asFlow()
        .mapToList(coroutineDispatchers.io)
        .map { dbRoutineList ->
            dbRoutineList.map { dbRoutine ->
                Routine(
                    id = dbRoutine.routineId,
                    name = dbRoutine.routineName,
                    exercisesCount = routineTemplates
                        // If this becomes a bottle-neck, move it into the DB layer
                        // do a JOIN and return the count
                        .selectExercisesForRoutine(dbRoutine.routineId)
                        .executeAsList()
                        .count()
                )
            }
        }

    override fun observeExercises(routineId: String): Flow<List<Exercise>> =
        routineTemplates
            .selectExercisesForRoutine(routineId)
            .asFlow()
            .mapToList(coroutineDispatchers.io)
            .map { dbExercises ->
                dbExercises.map {
                    Exercise(
                        id = it.exerciseId,
                        name = it.exerciseName,
                    )
                }
            }

    override fun observeExercisesByDate(routineId: String, date: String): Flow<List<Exercise>> {
        return historyQueries
            .selectRoutineHistoryByDate(date)
            .asFlow()
            .mapToList(coroutineDispatchers.io)
            .map { historyEntries ->
                historyEntries.flatMap { history ->
                    workoutExercises
                        .selectExercisesForHistory(history.historyId)
                        .executeAsList()
                        .map { dbExercise ->
                            Exercise(
                                id = dbExercise.exerciseId,
                                name = dbExercise.exerciseName,
                                sets = dbExercise.sets,
                                reps = dbExercise.reps
                            )
                        }
                }
            }
    }

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
        routineHistory: RoutineHistory,
    ) {
        withContext(coroutineDispatchers.io) {
            val historyId = idProvider.generate()

            fitForwardDatabase.transaction {
                historyQueries.insertRoutineHistory(
                    id = historyId,
                    routineId = routineHistory.routineId,
                    performedAt = routineHistory.performedAt,
                    durationSeconds = routineHistory.durationSeconds,
                    notes = routineHistory.notes
                )

                routineHistory.exercises.forEach { exercise ->
                    workoutExercises.insertRoutineHistoryExercise(
                        historyId = historyId,
                        exerciseId = exercise.id,
                        sets = exercise.sets,
                        reps = exercise.reps
                    )
                }
            }
        }
    }

    override fun observeRoutinesByDate(date: String): Flow<List<Routine>> =
        historyQueries
            .selectRoutinesByDate(date)
            .asFlow()
            .mapToList(coroutineDispatchers.io)
            .map { dbRoutines ->
                dbRoutines.map {
                    Routine(
                        id = it.routineId,
                        name = it.routineName,
                        exercisesCount = routineTemplates
                            .selectExercisesForRoutine(it.routineId)
                            .executeAsList()
                            .count()
                    )
                }
            }
}
