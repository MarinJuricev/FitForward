package home.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

interface RoutineRepository {

    fun observeRoutines(): Flow<List<Routine>>

    fun observeExercises(): Flow<List<Exercise>>

    suspend fun upsertRoutine(routine: Routine)

    suspend fun deleteRoutine(id: String)
}

@OptIn(ExperimentalCoroutinesApi::class)
class InMemoryRoutineRepository(
    coroutineScope: CoroutineScope,
) : RoutineRepository {

    private val _routines = MutableStateFlow<List<Routine>>(initialRoutines)

    private val routines = _routines
        .stateIn(coroutineScope, SharingStarted.Eagerly, initialRoutines)

    private val exercises = _routines.mapLatest { routines ->
        routines
            .filter { routine -> routine.isSelected }
            .flatMap { routine -> routine.exercises }
    }.stateIn(coroutineScope, SharingStarted.Eagerly, emptyList<Exercise>())

    override fun observeRoutines(): Flow<List<Routine>> = _routines

    override fun observeExercises(): Flow<List<Exercise>> = exercises

    override suspend fun upsertRoutine(routine: Routine) {
        _routines.update { existingRoutines ->
//                .takeIf { it.any { existing -> existing.id == routine.id } }
//                ?: (existingRoutines + routine)
            existingRoutines.map {
                if (it.id == routine.id) routine
                else it.copy(isSelected = false)
            }
        }
    }

    override suspend fun deleteRoutine(id: String) {
        _routines.update { it.filter { routine -> routine.id != id } }
    }
}

data class Routine(
    val id: String,
    val name: String,
    val isSelected: Boolean = false,
    val exercises: List<Exercise>,
)

data class Exercise(
    val id: String,
    val name: String
)

private val initialRoutines = listOf(
    Routine(
        id = "1",
        name = "Back",
        exercises = listOf(
            Exercise(id = "1", name = "Deadlift"),
            Exercise(id = "2", name = "Pull-up"),
            Exercise(id = "3", name = "Bent-over row"),
        )
    ),
    Routine(
        id = "2",
        name = "Legs",
        exercises = listOf(
            Exercise(id = "4", name = "Squat"),
            Exercise(id = "5", name = "Lunges"),
            Exercise(id = "6", name = "Leg press"),
        )
    ),
    Routine(
        id = "3",
        name = "Chest",
        exercises = listOf(
            Exercise(id = "7", name = "Bench press"),
            Exercise(id = "8", name = "Push-up"),
            Exercise(id = "9", name = "Chest fly"),
        )
    ),
)
