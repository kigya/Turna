package com.kigya.turna.presentation.ui.timer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TimerViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    var actualRecipeTimeMillis: Long
        get() = savedStateHandle[CURRENT_RECIPE_TIME] ?: 0L
        set(value) = savedStateHandle.set(CURRENT_RECIPE_TIME, value)

    private val _actualMilliseconds = MutableStateFlow(0L)
    val actualMilliseconds: StateFlow<Long>
        get() = _actualMilliseconds

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean>
        get() = _isRunning

    fun setTimer(milliseconds: Long) {
        _actualMilliseconds.value = milliseconds
    }

    fun setRunningState(isRunning: Boolean) {
        _isRunning.value = isRunning
    }

    private companion object {
        const val CURRENT_RECIPE_TIME = "current_recipe_time"
    }
}