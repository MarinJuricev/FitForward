package exercisedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class ExerciseDetailViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val exercise = savedStateHandle.getStateFlow(
        EXERCISE_ID,
        ""
    )
}

private const val EXERCISE_ID = "exerciseId"