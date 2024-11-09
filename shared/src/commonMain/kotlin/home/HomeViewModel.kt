package home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

class HomeViewModel(
    calendarPresenterFactory: CalendarPresenterFactory
) : ViewModel() {

    val calendarState = calendarPresenterFactory.create(viewModelScope)
}