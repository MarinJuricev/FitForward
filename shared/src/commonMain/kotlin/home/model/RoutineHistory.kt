package home.model

data class RoutineHistory(
    val routineId: String,
    val performedAt: String,
    val durationSeconds: Long,
    val notes: String = "",
)
