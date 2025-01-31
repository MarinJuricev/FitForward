package home.model

data class RoutineHistory(
    val routineId: String,
    val performedAt: String,
    val exercises: List<Exercise>,
    val durationSeconds: Long? = null,
    val notes: String? = null,
)
