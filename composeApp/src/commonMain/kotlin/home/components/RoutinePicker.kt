package home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.ArcMode
import androidx.compose.animation.core.ExperimentalAnimationSpecApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import design.FitBodyMediumText
import design.FitBodySmallText
import design.FitCard
import design.FitOutlinedButton
import design.FitTitleMediumText
import design.LocalNavAnimatedVisibilityScope
import design.LocalSharedTransitionScope
import home.presenter.RoutineInfo
import home.presenter.RoutinePickerEvent.NavigateToRoutines
import home.presenter.RoutinePickerEvent.RoutineSelected
import home.presenter.RoutinePickerState
import kotlinx.coroutines.launch
import navigation.arcTransform
import navigation.fitBoundsTransform

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class,
    ExperimentalAnimationSpecApi::class
)
@Composable
fun RoutinePicker(
    routineState: RoutinePickerState,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: error("LocalSharedTransitionScope not provided")
    val animatedContentScope = LocalNavAnimatedVisibilityScope.current
        ?: error("LocalNavAnimatedVisibilityScope not provided")

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FitCard(
            modifier = Modifier
                .weight(0.7f)
                .padding(end = 16.dp),
            onClick = { scope.launch { showBottomSheet = true } }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                FitBodyMediumText(
                    routineState.selectedRoutine?.name ?: "Select a routine"
                )
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                )
            }

        }

        with(sharedTransitionScope) {
            FitOutlinedButton(
                modifier = Modifier.weight(0.3f)
                    .renderInSharedTransitionScopeOverlay()
                    .sharedBounds(
                        sharedContentState = rememberSharedContentState(key = ROUTINE_KEY),
                        animatedVisibilityScope = animatedContentScope,
                        placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize,
                        boundsTransform = arcTransform(),
                    ),
                onClick = {
                    routineState.onRoutineEvent(NavigateToRoutines)
                }
            ) {
                FitBodyMediumText(
                    modifier = Modifier.sharedElement(
                        sharedTransitionScope.rememberSharedContentState(key = ROUTINE_TEXT_KEY),
                        animatedVisibilityScope = animatedContentScope,
                        boundsTransform = arcTransform(),
                    ),
                    text = "Routines"
                )
            }
        }
    }
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            FitTitleMediumText(
                text = "Select a routine",
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .align(Alignment.CenterHorizontally),
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(routineState.routines, key = { it.id }) { routine ->
                    RoutineItem(
                        routine = routine,
                        selectedRoutine = routineState.selectedRoutine,
                        onClick = {
                            routineState.onRoutineEvent(RoutineSelected(routine))
                            scope
                                .launch { sheetState.hide() }
                                .invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        showBottomSheet = false
                                    }
                                }
                        }
                    )
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun RoutineItem(
    routine: RoutineInfo,
    selectedRoutine: RoutineInfo?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AnimatedVisibility(routine == selectedRoutine) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            FitBodyMediumText(
                text = routine.name,
                modifier = Modifier.fillMaxWidth(),
            )
            FitBodySmallText(
                text = routine.description,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

const val ROUTINE_KEY = "routine"
const val ROUTINE_TEXT_KEY = "routineText"
