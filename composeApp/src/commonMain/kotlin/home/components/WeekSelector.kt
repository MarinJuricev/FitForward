package home.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.isTraceInProgress
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            val selectedTransition = updateTransition(day.isSelected)
            val targetFontWeight by selectedTransition.animateInt { isSelected ->
                if (isSelected) FontWeight.Bold.weight else FontWeight.Normal.weight
            }
            val fontSize by selectedTransition.animateInt { isSelected ->
                if (isSelected) 18 else 14
            }
            val dayValueBackground by selectedTransition.animateColor { isSelected ->
                if (isSelected) MaterialTheme.colorScheme.inversePrimary else Color.Transparent
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onDayClick(day) }
                    .padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                FitNo(text = day.value)
                Text(
                    text = day.name,
                    fontSize = fontSize.sp,
                    fontWeight = FontWeight(targetFontWeight),
                    modifier = Modifier
                        .background(
                            color = dayValueBackground,
                            shape = MaterialTheme.shapes.extraSmall,
                        )
                        .padding(vertical = 2.dp, horizontal = 8.dp),
                )
            }
        }
    }
}