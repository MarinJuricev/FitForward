package org.metalroads.fitforward.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import home.components.WeekSelector
import home.model.DayInfo
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

@Composable
@Preview
private fun WeekSelectorPreview() {
    val date = Clock.System.now().toLocalDateTime(TimeZone.UTC).date

    val days = remember {
        mutableStateListOf(
            DayInfo(name = "Mon", value = "1", date = date),
            DayInfo(name = "Tue", value = "2", date = date.plus(DatePeriod(days = 1))),
            DayInfo(
                name = "Wen",
                value = "3",
                date = date.plus(DatePeriod(days = 1)),
                isSelected = true
            ),
            DayInfo(name = "Thur", value = "4", date = date.plus(DatePeriod(days = 2))),
            DayInfo(name = "Fri", value = "5", date = date.plus(DatePeriod(days = 3))),
            DayInfo(name = "Sun", value = "6", date = date.plus(DatePeriod(days = 4))),
            DayInfo(name = "Sat", value = "7", date = date.plus(DatePeriod(days = 5))),
        )
    }

    WeekSelector(
        days = days,
        onDayClick = { clickedDay ->
            val clickedIndex = days.indexOf(clickedDay)

//            days = days[clickedIndex] = days[clickedIndex].copy(isSelected = !clickedDay.isSelected)
        }
    )
}
