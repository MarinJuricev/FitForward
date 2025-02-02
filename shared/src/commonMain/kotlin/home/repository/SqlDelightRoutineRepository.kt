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

    override fun observeExercises(routineId: String): Flow<List<Exercise>> = routineTemplatesQueries
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

    override fun observeWorkoutHistoryByDate(date: String): Flow<List<Exercise>> =
        workoutHistoryQueries
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

    override suspend fun upsertRoutine(routine: Routine) = withContext(coroutineDispatchers.io) {
        routineQueries.upsertRoutine(
            id = routine.id,
            name = routine.name,
        )
    }

    override suspend fun deleteRoutine(id: String) = withContext(coroutineDispatchers.io) {
        routineQueries.deleteRoutine(id)
    }


    override suspend fun upsertRoutineHistory(
        routineHistory: RoutineHistory,
    ) = withContext(coroutineDispatchers.io) {
        val historyId = routineHistory.id

        fitForwardDatabase.transaction {
            workoutHistoryQueries.upsertRoutineHistory(
                id = historyId,
                routineId = routineHistory.routineId,
                performedAt = routineHistory.performedAt,
                durationSeconds = routineHistory.durationSeconds,
                notes = routineHistory.notes
            )

            routineHistory.exercises.forEach { exercise ->
                workoutExercisesQueries.upsertRoutineHistoryExercise(
                    historyId = historyId,
                    exerciseId = exercise.id,
                    sets = exercise.sets,
                    reps = exercise.reps
                )
            }
        }
    }

    override suspend fun deleteWorkoutExerciseByDate(date: String) =
        withContext(coroutineDispatchers.io) {
            workoutHistoryQueries.deleteRoutineHistoryByDate(date)
            workoutExercisesQueries.deleteWorkoutExercisesForHistoryByDate(performedAt = date)
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