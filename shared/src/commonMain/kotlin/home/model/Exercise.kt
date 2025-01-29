package home.model

data class Exercise(
    val id: String,
    val name: String,
    val sets: Int? = null,
    val reps: Int? = null
)
