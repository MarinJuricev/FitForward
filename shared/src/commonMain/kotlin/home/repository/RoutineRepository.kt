package home.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

interface RoutineRepository {

    fun observeRoutines(): Flow<List<Routine>>

    fun upsertRoutine(routine: Routine)

    fun deleteRoutine(id: String)
}

class InMemoryRoutineRepository : RoutineRepository {

    val initialRoutines = listOf(
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

    private val routines = MutableStateFlow<List<Routine>>(initialRoutines)

    override fun observeRoutines(): Flow<List<Routine>> = routines

    override fun upsertRoutine(routine: Routine) {
        routines.update { it.plus(routine) }
    }

    override fun deleteRoutine(id: String) {
        routines.update { it.filter { routine -> routine.id != id } }
    }
}

data class Routine(
    val id: String,
    val name: String,
    val exercises: List<Exercise>,
)

data class Exercise(
    val id: String,
    val name: String
)