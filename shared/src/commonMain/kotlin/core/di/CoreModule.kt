@file:OptIn(DelicateCoroutinesApi::class)

package core.di

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import com.fitforward.data.FitForwardDatabase
import core.AppCoroutineDispatchers
import core.DateProvider
import db.DriverFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.dsl.module

expect val ioDispatcher: CoroutineDispatcher

val coreModule = module {
    factory {
        DateProvider {
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        }
    }
    factory {
        AppCoroutineDispatchers(
//            io = ioDispatcher,
            // For now just live with this, we won't be utilizing the IO dispatcher
            // that often, we rely on the libraries that expose suspend functions to
            // to be main "safe"
            io = Dispatchers.Default,
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
        )
    }
}

object FitForwardSchema :
    SqlSchema<QueryResult.Value<Unit>> by FitForwardDatabase.Schema.synchronous() {
    override fun create(driver: SqlDriver): QueryResult.Value<Unit> {
        FitForwardDatabase.Schema.create(driver)

        runBlocking {
            FitForwardDatabase(driver).seedDatabase()
        }
        return QueryResult.Unit
    }
}

private suspend fun FitForwardDatabase.seedDatabase() {
    if (routineQueries.selectAllRoutines().executeAsList().isNotEmpty()) return
    // Insert Routines
    routineQueries.insertRoutine("1", "Leg Day")
    routineQueries.insertRoutine("2", "Push Day")
    routineQueries.insertRoutine("3", "Pull Day")

    // Insert Exercises
    exerciseQueries.insertExercise("1", "Squat")
    exerciseQueries.insertExercise("2", "Lunges")
    exerciseQueries.insertExercise("3", "Deadlift")
    exerciseQueries.insertExercise("4", "Bench Press")
    exerciseQueries.insertExercise("5", "Overhead Press")
    exerciseQueries.insertExercise("6", "Pull-up")
    exerciseQueries.insertExercise("7", "Barbell Row")

    // Link Routines with Exercises
    routineExerciseQueries.insertRoutineExercise("1", "1") // Leg Day -> Squat
    routineExerciseQueries.insertRoutineExercise("1", "2") // Leg Day -> Lunges
    routineExerciseQueries.insertRoutineExercise("2", "4") // Push Day -> Bench Press
    routineExerciseQueries.insertRoutineExercise("2", "5") // Push Day -> Overhead Press
    routineExerciseQueries.insertRoutineExercise("3", "3") // Pull Day -> Deadlift
    routineExerciseQueries.insertRoutineExercise("3", "6") // Pull Day -> Pull-up
    routineExerciseQueries.insertRoutineExercise("3", "7") // Pull Day -> Barbell Row}
}
