package design

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

@Composable
fun FitTitleLargeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
    textAlign: TextAlign? = null
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.titleLarge,
        color = color,
        textAlign = textAlign
    )
}

@Composable
fun FitTitleMediumText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
    textAlign: TextAlign? = null
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.titleMedium,
        color = color,
        textAlign = textAlign
    )
}

@Composable
fun FitBodyLargeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
    textAlign: TextAlign? = null
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodyLarge,
        color = color,
        textAlign = textAlign
    )
}

@Composable
fun FitBodyMediumText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
    textAlign: TextAlign? = null
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium,
        color = color,
        textAlign = textAlign
    )
}

@Composable
fun FitBodySmallText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
    textAlign: TextAlign? = null
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodySmall,
        color = color,
        textAlign = textAlign
    )
}