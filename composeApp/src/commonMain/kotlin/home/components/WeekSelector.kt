package home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import home.model.DayInfo

@Composable
fun WeekSelector(
    days: List<DayInfo>,
    onDayClick: (DayInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.background(Color.Red),
    ) {
        days.forEach { day ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onDayClick(day) },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = day.name)
                Text(text = day.value)

                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .size(6.dp)
                        .then(
                            if (day.isSelected) Modifier.clip(CircleShape).background(Color.Blue)
                            else Modifier
                        )
                )
            }
        }
    }
}