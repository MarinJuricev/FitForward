package home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import home.presenter.CalendarState

@Composable
fun FitCalendarPicker(
    state: CalendarState,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { 3 }
    )

    HorizontalPager(
        modifier = modifier.fillMaxWidth(),
        state = pagerState,
    ) { pageIndex ->
        val startIndex = pageIndex * 7
        val endIndex = minOf(startIndex + 7, state.days.size)
        val daysForPage = state.days.subList(startIndex, endIndex)

        WeekSelector(
            days = daysForPage,
            onDayClick = state.onDayClick
        )
    }

}
