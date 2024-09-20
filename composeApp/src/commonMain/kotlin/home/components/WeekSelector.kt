package home.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import home.model.DayInfo

@Composable
fun WeekSelector(
    days: List<DayInfo>,
    onDayClick: (DayInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.background(MaterialTheme.colorScheme.onPrimary),
    ) {
        days.fastForEach { day ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onDayClick(day) },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(text = day.name)
                Text(text = day.value)

                val backgroundColor by animateColorAsState(
                    targetValue = if (day.isSelected) MaterialTheme.colorScheme.secondary else Color.Transparent
                )

                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(backgroundColor)
                )
            }
        }
    }
}