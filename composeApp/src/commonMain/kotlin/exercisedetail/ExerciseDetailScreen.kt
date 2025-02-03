package exercisedetail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable
import navigation.Route

@Serializable
data class ExerciseDetailRoute(
    val exerciseId: String,
) : Route

@Composable
fun ExerciseDetailsScreen() {
    Text("Exercise Details")
}