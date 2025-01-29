package home.model

data class RoutineHistory(
    val routineId: String,
    val performedAt: String,
    val durationSeconds: Int? = null,
    val notes: String? = null,
)
