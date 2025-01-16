package home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import home.presenter.RoutinePickerState

@Composable
fun RoutinePicker(
    routineState: RoutinePickerState,
    modifier: Modifier = Modifier
) {
    val selectedItem by remember(routineState.routines) {
        mutableStateOf(routineState.routines.find { it.isSelected })
    }
    var isMenuExpanded by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {

        selectedItem?.let {
            Text("Routine: ${it.name}")
        }
        Text(routineState.routines.toString())
//        selectedItem?.let { item ->
//            Text(
//                modifier = Modifier.weight(0.8f),
//                text = item.name
//            )
//        }
//        DropdownMenu(
//            expanded = isMenuExpanded,
//            modifier = Modifier,
//            onDismissRequest = { isMenuExpanded = !isMenuExpanded },
//            offset = DpOffset.Zero,
//            scrollState = rememberScrollState(),
//            properties = PopupProperties(),
//            shape = RoundedCornerShape(8.dp),
//            containerColor = Color.Black,
//            tonalElevation = 4.dp,
//            shadowElevation = 4.dp
//        ) {
//            Text("Item 1")
//        }

    }

}
