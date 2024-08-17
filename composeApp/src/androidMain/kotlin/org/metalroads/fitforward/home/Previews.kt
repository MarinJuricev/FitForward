package org.metalroads.fitforward.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import home.components.WeekSelector
import home.model.DayInfo

@Composable
@Preview
private fun WeekSelectorPreview() {


    val days = remember {
        mutableStateListOf(
            DayInfo(name = "Mon", "1"),
            DayInfo(name = "Tue", "2"),
            DayInfo(name = "", "3", isSelected = true),
            DayInfo(name = "Thur", "4"),
            DayInfo(name = "Fri", "5"),
            DayInfo(name = "Sun", "6"),
            DayInfo(name = "Sat", "7"),
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
