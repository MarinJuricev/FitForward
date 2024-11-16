package home.model

import kotlinx.datetime.LocalDate

data class DayInfo(
    val date: LocalDate,
    val name: String,
    val value: String,
    val isSelected: Boolean = false,
)
