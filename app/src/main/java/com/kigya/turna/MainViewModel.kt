package com.kigya.turna

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import java.sql.Time
import kotlin.math.absoluteValue
import kotlin.time.Duration.Companion.milliseconds

const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"

class MainViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    var currentIndex
        get() = savedStateHandle[CURRENT_INDEX_KEY] ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    val recipeBank = listOf(
        Recipe(R.string.easy_name, R.string.easy_desc, Time(120 * 1000)),
        Recipe(R.string.medium_name, R.string.medium_desc, Time(150 * 1000)),
        Recipe(R.string.well_name, R.string.well_desc, Time(180 * 1000)),
        Recipe(R.string.sunny_side_up_name, R.string.sunny_side_up_desc, Time(150 * 1000)),
        Recipe(R.string.hard_name, R.string.hard_desc, Time(200 * 1000)),
    )

    val currentDescriptionId: Int
        get() = recipeBank[currentIndex].descriptionId

    val currentCookTime: Long
        get() = recipeBank[currentIndex].time.time.milliseconds.inWholeMilliseconds

}