@file:OptIn(ExperimentalUuidApi::class)

package core.di

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import com.fitforward.data.FitForwardDatabase
import com.fitforward.data.Workout_exercise
import core.AppCoroutineDispatchers
import core.DateProvider
import core.IdProvider
import db.DriverFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.dsl.module
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

expect val ioDispatcher: CoroutineDispatcher

val coreModule = module {
    factory {
        DateProvider {
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        }
    }
    factory {
        IdProvider { Uuid.random().toString() }
    }
    factory {
        AppCoroutineDispatchers(
//            io = ioDispatcher,
            // For now just live with this, we won't be utilizing the IO dispatcher
            // that often, we rely on the libraries that expose suspend functions to
            // to be main "safe"
            io = Dispatchers.IO,
            computation = Dispatchers.Default,
            main = Dispatchers.Main,
        )
    }

    single {
        val driver = get<DriverFactory>().createDriver().apply {
            // Enable foreign keys
            execute(null, "PRAGMA foreign_keys = ON", 0)
        }

        // Seed the data
        FitForwardSchema.create(driver)
        FitForwardDatabase(
            driver = driver,
            workout_exerciseAdapter = Workout_exercise.Adapter(
                setsAdapter = IntColumnAdapter,
                repsAdapter = IntColumnAdapter,
            )
        )
    }
}

object FitForwardSchema :
    SqlSchema<QueryResult.Value<Unit>> by FitForwardDatabase.Schema.synchronous() {
    override fun create(driver: SqlDriver): QueryResult.Value<Unit> {
        FitForwardDatabase.Schema.create(driver)

        runBlocking {
            FitForwardDatabase(
                driver = driver,
                workout_exerciseAdapter = Workout_exercise.Adapter(
                    setsAdapter = IntColumnAdapter,
                    repsAdapter = IntColumnAdapter,
                )
            ).seedDatabase()
        }
        return QueryResult.Unit
    }
}

private suspend fun FitForwardDatabase.seedDatabase() {
    // Check if the database is already seeded
    if (routineQueries.selectAllRoutines().executeAsList().isNotEmpty()) return

    // Insert Routines
    routineQueries.upsertRoutine(
        id = "1",
        name = "Leg Day",
    )
    routineQueries.upsertRoutine(
        id = "2",
        name = "Push Day",
    )
    routineQueries.upsertRoutine(
        id = "3",
        name = "Pull Day",
    )

    // upsert Exercises
    exerciseQueries.upsertExercise(
        id = "1",
        name = "Squat",
    )
    exerciseQueries.upsertExercise(
        id = "2",
        name = "Lunges"
    )
    exerciseQueries.upsertExercise(
        id = "3",
        name = "Deadlift"
    )
    exerciseQueries.upsertExercise(
        id = "4",
        name = "Bench Press"
    )
    exerciseQueries.upsertExercise(
        id = "5",
        name = "Overhead Press"
    )
    exerciseQueries.upsertExercise(
        id = "6",
        name = "Pull-up"
    )
    exerciseQueries.upsertExercise(
        id = "7",
        name = "Barbell Row"
    )

    // Link Routines with Exercises
    routineTemplateQueries.upsertRoutineExercise(
        routineId = "1",
        exerciseId = "1",
        position = 1,
        sets = 3,
        reps = 10
    ) // Leg Day -> Squat
    routineTemplateQueries.upsertRoutineExercise(
        routineId = "1",
        exerciseId = "2",
        position = 2,
        sets = 3,
        reps = 1
    ) // Leg Day -> Lunges
    routineTemplateQueries.upsertRoutineExercise(
        routineId = "2",
        exerciseId = "4",
        position = 1,
        sets = 4,
        reps = 8
    ) // Push Day -> Bench Press
    routineTemplateQueries.upsertRoutineExercise(
        routineId = "2",
        exerciseId = "5",
        position = 2,
        sets = 3,
        reps = 10,
    ) // Push Day -> Overhead Press
    routineTemplateQueries.upsertRoutineExercise(
        routineId = "3",
        exerciseId = "3",
        position = 1,
        sets = 3,
        reps = 5
    ) // Pull Day -> Deadlift
    routineTemplateQueries.upsertRoutineExercise(
        routineId = "3",
        exerciseId = "6",
        position = 2,
        sets = 3,
        reps = 8
    ) // Pull Day -> Pull-up
    routineTemplateQueries.upsertRoutineExercise(
        routineId = "3",
        exerciseId = "7",
        position = 3,
        sets = 3,
        reps = 10,
    ) // Pull Day -> Barbell Row

    // upsert Routine History
    workoutHistoryQueries.upsertRoutineHistory(
        id = "1",
        routineId = "1",
        performedAt = "2023-10-25",
        durationSeconds = 3600,
        notes = "Great workout! Felt strong today."
    )
    workoutHistoryQueries.upsertRoutineHistory(
        id = "2",
        routineId = "2",
        performedAt = "2023-10-26",
        durationSeconds = 2700,
        notes = "Struggled with bench press but improved overhead press."
    )
    workoutHistoryQueries.upsertRoutineHistory(
        id = "3",
        routineId = "3",
        performedAt = "2023-10-27",
        durationSeconds = 3000,
        notes = "Deadlifts felt heavy, but pull-ups were smooth."
    )
}
