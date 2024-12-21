package home.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import home.presenter.RoutinePickerState

@Composable
fun RoutinePicker(
    routineState: RoutinePickerState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
    ) {
        Text(routineState.routines.toString())
    }

}
