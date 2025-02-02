package exercisedetail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable
import navigation.Route

@Serializable
object ExerciseDetailRoute : Route

@Composable
fun ExerciseDetailsScreen() {
    Text("Exercise Details")
}