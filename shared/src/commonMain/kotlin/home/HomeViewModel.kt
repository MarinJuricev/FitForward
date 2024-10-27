package home

import androidx.lifecycle.ViewModel
import me.tatarka.inject.annotations.Inject

@Inject
class HomeViewModel(
    private val calendarPresenterFactory: CalendarPresenterFactory
) : ViewModel() {

}