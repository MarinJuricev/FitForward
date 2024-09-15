package home

import androidx.compose.runtime.Composable
import com.slack.circuit.retained.rememberRetained
import core.DateProvider
import home.model.DayInfo
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus

@Composable
fun CalendarPresenter(
    dateProvider: DateProvider,
): List<DayInfo> {
    val availableDates = rememberRetained {
        generateDataSet(dateProvider.generate())
    }

    return availableDates
}

private fun generateDataSet(
    today: LocalDate
): List<DayInfo> {
    val startOfCurrentWeek = today.minus(today.dayOfWeek.ordinal, DateTimeUnit.DAY)

    return generateWeekDays(startOfCurrentWeek, today)
}

private fun generateWeekDays(
    startDate: LocalDate,
    today: LocalDate
): List<DayInfo> = (START_OF_PREVIOUS_WEEK..END_OF_NEXT_WEEK).map { offset ->
    val date = startDate.plus(offset, DateTimeUnit.DAY)

    DayInfo(
        name = date.dayOfMonth.toString(),
        value = date.dayOfWeek.name.take(3)
            .lowercase()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
        isSelected = date == today,
    )
}

private const val START_OF_PREVIOUS_WEEK = -7
private const val END_OF_NEXT_WEEK = 13
