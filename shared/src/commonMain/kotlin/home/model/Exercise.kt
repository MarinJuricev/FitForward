package home.model

import core.model.Weight

data class Exercise(
    val id: String,
    val name: String,
    val sets: Int? = null,
    val reps: Int? = null,
    val weight: Weight = Weight.IMPERIAL,
)
