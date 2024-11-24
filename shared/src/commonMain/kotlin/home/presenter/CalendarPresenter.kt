package home.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import core.DateProvider
import home.model.DayInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus

data class CalendarState(
    val days: List<DayInfo>,
    val onDayClick: (DayInfo) -> Unit,
)

class CalendarPresenterFactory(
    private val dateProvider: DateProvider,
) {

    fun create(
        coroutineScope: CoroutineScope
    ): StateFlow<CalendarState> = coroutineScope
        .launchMolecule(RecompositionMode.Immediate) {
            CalendarPresenter(dateProvider)
        }
}

@Composable
internal fun CalendarPresenter(
    dateProvider: DateProvider,
): CalendarState {
    var availableDates by remember {
        mutableStateOf(generateDataSet(dateProvider.generate()))
    }

    return CalendarState(
        days = availableDates,
        onDayClick = { selectedDay ->
            availableDates = availableDates.map { day ->
                day.copy(isSelected = day == selectedDay)
            }
        },
    )
}

private fun generateDataSet(
    today: LocalDate
): List<DayInfo> {
    val startOfCurrentWeek = today.minus(today.dayOfWeek.ordinal, DateTimeUnit.DAY)

    return generateWeekDays(startOfCurrentWeek, today)
}

private fun generateWeekDays(
    startDate: LocalDate, today: LocalDate
): List<DayInfo> = (START_OF_PREVIOUS_WEEK..END_OF_NEXT_WEEK).map { offset ->
    val date = startDate.plus(offset, DateTimeUnit.DAY)

    DayInfo(
        date = date,
        name = date.dayOfMonth.toString(),
        value = date.dayOfWeek.name.take(3).lowercase()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
        isSelected = date == today,
    )
}

private const val START_OF_PREVIOUS_WEEK = -7
private const val END_OF_NEXT_WEEK = 13
