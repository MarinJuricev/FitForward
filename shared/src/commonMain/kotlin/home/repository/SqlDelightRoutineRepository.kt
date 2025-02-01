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

class SqlDelightRoutineRepository(
    private val fitForwardDatabase: FitForwardDatabase,
    private val idProvider: IdProvider,
    private val coroutineDispatchers: AppCoroutineDispatchers,
) : RoutineRepository {

    private val routineQueries = fitForwardDatabase.routineQueries
    private val routineTemplatesQueries = fitForwardDatabase.routineTemplateQueries
    private val workoutHistoryQueries = fitForwardDatabase.workoutHistoryQueries
    private val workoutExercisesQueries = fitForwardDatabase.workoutExerciseQueries

    override fun observeRoutines(): Flow<List<Routine>> = routineQueries
        .selectAllRoutinesWithExerciseCount()
        .asFlow()
        .mapToList(coroutineDispatchers.io)
        .map { dbRoutineList ->
            dbRoutineList.map { dbRoutine ->
                Routine(
                    id = dbRoutine.routineId,
                    name = dbRoutine.routineName,
                    exercisesCount = dbRoutine.exerciseCount.toInt()
                )
            }
        }

    override fun observeExercises(routineId: String): Flow<List<Exercise>> =
        routineTemplatesQueries
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

    override fun observeWorkoutHistoryByDate(date: String): Flow<List<Exercise>> {
        return workoutHistoryQueries
            .selectRoutineHistoryByDate(date)
            .asFlow()
            .mapToList(coroutineDispatchers.io)
            .map { historyEntries ->
                historyEntries.flatMap { history ->
                    workoutExercisesQueries
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
                workoutHistoryQueries.insertRoutineHistory(
                    id = historyId,
                    routineId = routineHistory.routineId,
                    performedAt = routineHistory.performedAt,
                    durationSeconds = routineHistory.durationSeconds,
                    notes = routineHistory.notes
                )

                routineHistory.exercises.forEach { exercise ->
                    workoutExercisesQueries.insertRoutineHistoryExercise(
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
        workoutHistoryQueries
            .selectRoutinesByDate(date)
            .asFlow()
            .mapToList(coroutineDispatchers.io)
            .map { dbRoutines ->
                dbRoutines.map {
                    Routine(
                        id = it.routineId,
                        name = it.routineName,
                        exercisesCount = routineTemplatesQueries
                            .selectExercisesForRoutine(it.routineId)
                            .executeAsList()
                            .count()
                    )
                }
            }
}