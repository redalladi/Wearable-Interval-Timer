package com.appspell.sportintervaltimer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.ScalingLazyListState
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.rememberScalingLazyListState
import com.appspell.sportintervaltimer.theme.MainTheme
import com.appspell.sportintervaltimer.timersetup.TimerSetupViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private val BUTTON_SIZE = 38.dp

@Composable
fun TimerSetupContent(
    viewModel: TimerSetupViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val listState = rememberScalingLazyListState()

    LaunchedEffect(Unit) {
        // TODO revisit navigation
        viewModel.navigation
            .onEach { newNavigationEvent ->
                navController.navigate(newNavigationEvent.route)
            }.launchIn(this)
    }

    MainTheme {
        Scaffold(
            timeText = {
                TimeText()
            },
            vignette = {
                Vignette(vignettePosition = VignettePosition.TopAndBottom)
            },
            positionIndicator = {
                PositionIndicator(
                    scalingLazyListState = listState
                )
            }
        ) {
            SetUpIntervalsContent(
                onStart = { viewModel.onSave() },
                listState = listState
            )
        }
    }
}

@Composable
private fun SetUpIntervalsContent(
    onStart: () -> Unit,
    listState: ScalingLazyListState,
) {
    ScalingLazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        item {
            TimerSetupStartButton(
                onClick = onStart,
            )
        }

    }
    // Scroll to the first item
    LaunchedEffect(Unit) {
        listState.scrollToItem(0)
    }
}

@Composable
private fun TimerSetupStartButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = { onClick() },
    ) {
        Text(text = stringResource(R.string.timer_setup_start_button))
    }
}