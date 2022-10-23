package com.appspell.sportintervaltimer.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appspell.sportintervaltimer.utils.toTimeString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val repository: TimerRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<TimerUiState>(TimerRepository.DEFAULT_STATE.toUIState())
    val uiState: StateFlow<TimerUiState> = _uiState

    private var lastCoroutineTimerJob: Job? = null

    init {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                repository.reset()
                onResume()
            }

            repository.dataState
                .collect { newState ->
                    _uiState.value = newState.toUIState()
                }
        }
    }

    fun onResume() {
        lastCoroutineTimerJob = viewModelScope.launch(Dispatchers.Default) {
            repository.resume()
        }
    }

    fun onPause() {
        lastCoroutineTimerJob?.cancel()
        repository.pause()
    }

    fun onSkip() {
        lastCoroutineTimerJob?.cancel()
        repository.skipAndPause()
        onResume()
    }

    private fun TimerDataState.toUIState() =
        TimerUiState(
            currentSet = this.currentSet,
            allSets = this.maxSets,
            type = this.currentType,
            time = this.timeLeftSeconds.toTimeString(),
            progress = this.currentProgress,
            isPaused = this.isPaused
        )
}